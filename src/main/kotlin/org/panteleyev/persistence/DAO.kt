/*
 * Copyright (c) 2017, Petr Panteleyev <petr@panteleyev.org>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.panteleyev.persistence

import org.panteleyev.persistence.DAOTypes.BAD_FIELD_TYPE
import org.panteleyev.persistence.annotations.Field
import org.panteleyev.persistence.annotations.ForeignKey
import org.panteleyev.persistence.annotations.Index
import org.panteleyev.persistence.annotations.RecordBuilder
import org.panteleyev.persistence.annotations.Table
import java.beans.IntrospectionException
import java.beans.Introspector
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.ArrayList
import java.util.Arrays
import java.util.HashSet
import java.util.Objects
import java.util.concurrent.ConcurrentHashMap
import javax.sql.DataSource
import kotlin.reflect.KClass

/**
 * Database record.
 */
interface Record {
    /**
     * ID of the record. Default implementation does not provide setter to support
     * immutable objects. Mutable records must override this property as var.
     */
    val id : Int
}

/**
 * Persistence API entry point.
 *
 * @param ds data source
 * @property dataSource
 * @constructor Creates new DAO object with optional data source.
 */
open class DAO (ds: DataSource?){
    private val primaryKeys = ConcurrentHashMap<Class<out Record>, Int>()
    private val insertSQL = ConcurrentHashMap<Class<out Record>, String>()
    private val updateSQL = ConcurrentHashMap<Class<out Record>, String>()
    private val deleteSQL = ConcurrentHashMap<Class<out Record>, String>()

    private var proxy: DAOProxy? = null

    var dataSource: DataSource? = null
        get() = field
        set(ds) {
            field = ds
            primaryKeys.clear()
            insertSQL.clear()
            deleteSQL.clear()
            proxy = setupProxy()
        }

    init {
        dataSource = ds
        proxy = setupProxy()
    }


    private fun setupProxy(): DAOProxy? {
        // TODO: figure out better way instead of class name check
        if (dataSource != null) {
            val dsClass = dataSource!!.javaClass.name.toLowerCase()

            if (dsClass.contains("mysql")) {
                return MySQLProxy()
            }

            if (dsClass.contains("sqlite")) {
                return SQLiteProxy()
            }

            throw IllegalStateException("Unsupported database type")
        } else {
            return null
        }
    }

    val connection: Connection
        get() = dataSource?.connection?:throw IllegalStateException("Not initialized")

    /**
     * Retrieves record from the database using record [id].
     */
    fun <T : Record> get(id: Int, clazz: KClass<out T>): T? {
        return get(id, clazz.java)
    }

    private fun <T : Record> get(id: Int, clazz: Class<out T>): T? {
        dataSource!!.connection.use { conn ->
            if (!clazz.isAnnotationPresent(Table::class.java)) {
                throw IllegalStateException(NOT_ANNOTATED)
            }
            val ann = clazz.getAnnotation(Table::class.java)

            val tableName = ann.value
            var idName = "id"

            for (method in clazz.methods) {
                val fieldAnn = method.getAnnotation(Field::class.java)
                if (fieldAnn != null && fieldAnn.primaryKey) {
                    idName = fieldAnn.value
                    break
                }
            }

            val sql = "SELECT * FROM $tableName WHERE $idName=?"
            val ps = conn.prepareStatement(sql)
            ps.setInt(1, id)
            val set = ps.executeQuery()

            return if (set.next()) fromSQL(set, clazz) else null
        }
    }


    /**
     * Retrieves all records of the specified [class][clazz].
     */
    fun <T : Record> getAll(clazz: KClass<T>): List<T> {
        val result = ArrayList<T>()

        dataSource!!.connection.use { conn ->
            if (!clazz.java.isAnnotationPresent(Table::class.java)) {
                throw IllegalStateException(NOT_ANNOTATED)
            }

            val tableName = clazz.java.getAnnotation(Table::class.java).value
            val ps = conn.prepareStatement("SELECT * FROM $tableName")
            val set = ps.executeQuery()
            while (set.next()) {
                result.add(fromSQL(set, clazz.java))
            }
        }

        return result
    }

    /**
     * Retrieves all records of the specified [class][clazz] and fills the [map].
     */
    fun <T : Record> getAll(clazz: KClass<T>, map: MutableMap<Int, T>) {
        dataSource!!.connection.use { conn ->
            if (!clazz.java.isAnnotationPresent(Table::class.java)) {
                throw IllegalStateException(NOT_ANNOTATED)
            }

            val tableName = clazz.java.getAnnotation(Table::class.java).value
            val ps = conn.prepareStatement("SELECT * FROM $tableName")
            val set = ps.executeQuery()
            while (set.next()) {
                val r = fromSQL(set, clazz.java)
                map.put(r.id, r)
            }
        }
    }

    private fun <T : Record> fromSQL(set: ResultSet, clazz: Class<T>): T {
        // First try to find @RecordBuilder constructor
        for (constructor in clazz.constructors) {
            if (constructor.isAnnotationPresent(RecordBuilder::class.java)) {
                return fromSQL(set, constructor)
            }
        }

        val result = clazz.newInstance()
        fromSQL(set, result)
        return result
    }

    private fun <T : Record> fromSQL(set: ResultSet, constructor: Constructor<*>): T {
        val paramCount = constructor.parameterCount

        val paramAnnotations = constructor.parameterAnnotations
        val paramTypes = constructor.parameterTypes
        val params = arrayOfNulls<Any>(paramCount)

        for (i in 0..paramCount - 1) {
            val fieldName = Arrays.stream(paramAnnotations[i])
                    .filter { a -> a is Field }
                    .findAny()
                    .map<String> { a -> (a as Field).value }
                    .orElseThrow<RuntimeException>({ RuntimeException() })

            params[i] = proxy!!.getFieldValue(fieldName, paramTypes[i], set)
        }

        return constructor.newInstance(*params) as T
    }

    private fun fromSQL(set: ResultSet, record: Record) {
        val bi = Introspector.getBeanInfo(record.javaClass)
        val pds = bi.propertyDescriptors
        for (pd in pds) {
            val getter = pd.readMethod
            val setter: Method? = pd.writeMethod

            val getterClass = getter.returnType

            if (setter != null) {
                val fld = getter.getAnnotation(Field::class.java)
                if (fld != null) {
                    setter.invoke(record, proxy!!.getFieldValue(fld.value, getterClass, set))
                }
            }
        }
    }

    private fun getEffectiveType(getter: Method): Class<*> {
        val rType = getter.genericReturnType

        if (rType is ParameterizedType) {
            val actualTypeArguments = rType.actualTypeArguments
            if (actualTypeArguments.size != 1) {
                throw IllegalStateException(BAD_FIELD_TYPE)
            } else {
                return actualTypeArguments[0] as Class<*>
            }
        } else {
            return rType as Class<*>
        }
    }

    /**
     * Creates tables of the [specified classes][tables] according to their annotations.
     */
    fun createTables(tables: List<KClass<out Record>>) {
        if (dataSource == null) {
            throw IllegalStateException("Database not opened")
        }

        try {
            dataSource!!.connection.use { conn ->
                conn.createStatement().use { st ->
                    // Step 1: drop tables in reverse order
                    for (index in tables.indices.reversed()) {
                        val cl = tables[index]
                        if (!cl.java.isAnnotationPresent(Table::class.java)) {
                            throw IllegalStateException(NOT_ANNOTATED)
                        }

                        val table = cl.java.getAnnotation(Table::class.java)
                        st.executeUpdate("DROP TABLE IF EXISTS ${table.value}")
                    }

                    // Step 2: create new tables in natural order
                    for (cl in tables) {
                        val table = cl.java.getAnnotation(Table::class.java)

                        try {
                            val b = StringBuilder("CREATE TABLE IF NOT EXISTS ${table.value} (")

                            val bi = Introspector.getBeanInfo(cl.java)
                            val pds = bi.propertyDescriptors

                            val constraints = mutableListOf<String>()

                            val indexed = HashSet<Method>()

                            var first = true
                            for (pd in pds) {
                                val getter = pd.readMethod
                                if (getter != null && getter.isAnnotationPresent(Field::class.java)) {
                                    val fld = getter.getAnnotation(Field::class.java)
                                    val fName = fld.value

                                    val getterType = getEffectiveType(getter)
                                    val typeName = getterType.typeName

                                    if (!first) {
                                        b.append(",")
                                    }
                                    first = false

                                    b.append(fName)
                                            .append(" ")
                                            .append(proxy!!.getColumnString(fld,
                                                    getter.getAnnotation(ForeignKey::class.java), typeName, constraints))

                                    if (getter.isAnnotationPresent(Index::class.java)) {
                                        indexed.add(getter)
                                    }
                                }
                            }

                            if (!constraints.isEmpty()) {
                                b.append(",")
                                b.append(constraints.joinToString(","))
                            }

                            b.append(")")

                            st.executeUpdate(b.toString())

                            // Create indexes
                            for (getter in indexed) {
                                st.executeUpdate(proxy!!.buildIndex(table, getter))
                            }

                        } catch (ex: IntrospectionException) {
                            throw RuntimeException(ex)
                        }

                    }
                }
            }
        } catch (ex: SQLException) {
            throw RuntimeException(ex)
        }

    }

    private fun getInsertSQL(record: Record): String {
        return insertSQL.computeIfAbsent(record.javaClass) { clazz ->
            val b = StringBuilder("INSERT INTO ")

            val table = clazz.getAnnotation(Table::class.java) ?: throw IllegalStateException("Class " + clazz.name + " is not properly annotated")

            b.append(table.value).append(" (")

            var fCount = 0

            val bi = Introspector.getBeanInfo(record.javaClass)
            val pds = bi.propertyDescriptors
            for (pd in pds) {
                val getter = pd.readMethod
                if (getter != null) {
                    val fld = getter.getAnnotation(Field::class.java)
                    if (fld != null) {
                        if (fCount != 0) {
                            b.append(",")
                        }
                        b.append(fld.value)
                        fCount++
                    }
                }
            }

            if (fCount == 0) {
                throw IllegalStateException("No fields")
            }

            b.append(") VALUES (")

            while (fCount != 0) {
                b.append("?")
                if (fCount != 1) {
                    b.append(",")
                }
                fCount--
            }

            b.append(")")
            b.toString()
        }
    }

    private fun getUpdateSQL(record: Record): String {
        return updateSQL.computeIfAbsent(record.javaClass) { clazz ->
            val b = StringBuilder("update ")

            val table = clazz.getAnnotation(Table::class.java) ?: throw IllegalStateException(NOT_ANNOTATED)

            b.append(table.value).append(" set ")

            var fCount = 0

            val bi = Introspector.getBeanInfo(record.javaClass)
            val pds = bi.propertyDescriptors
            for (pd in pds) {
                val getter = pd.readMethod
                if (getter != null) {
                    val fld = getter.getAnnotation(Field::class.java)
                    if (fld != null && !fld.primaryKey) {
                        if (fCount != 0) {
                            b.append(", ")
                        }
                        b.append(fld.value)
                                .append("=?")
                        fCount++
                    }
                }
            }

            if (fCount == 0) {
                throw IllegalStateException("No fields")
            }

            b.append(" WHERE id=?")

            b.toString()
        }
    }

    private fun getDeleteSQL(clazz: Class<out Record>): String {
        return deleteSQL.computeIfAbsent(clazz) { cl ->
            val table = cl.getAnnotation(Table::class.java) ?: throw IllegalStateException(NOT_ANNOTATED)
            val b = StringBuilder("delete from ${table.value} where ")

            var idName: String? = null

            val bi = Introspector.getBeanInfo(cl)
            val pds = bi.propertyDescriptors
            for (pd in pds) {
                val getter = pd.readMethod
                if (getter != null) {
                    val fld = getter.getAnnotation(Field::class.java)
                    if (fld != null && fld.primaryKey) {
                        idName = fld.value
                        break
                    }
                }
            }

            if (idName == null) {
                throw IllegalStateException(NOT_ANNOTATED)
            }

            b.append(idName)
                    .append("=?")

            b.toString()
        }
    }

    private fun getDeleteSQL(record: Record): String {
        return getDeleteSQL(record.javaClass)
    }

    private fun setData(record: Record, st: PreparedStatement, update: Boolean) {
        val bi = Introspector.getBeanInfo(record.javaClass)
        val pds = bi.propertyDescriptors

        var index = 1
        for (pd in pds) {
            val getter = pd.readMethod
            if (getter != null && getter.isAnnotationPresent(Field::class.java)) {
                // if update skip ID at this point
                val fld = getter.getAnnotation(Field::class.java)
                if (update && fld.primaryKey) {
                    continue
                }

                val value: Any? = getter.invoke(record)
                val getterClass = getter.returnType

                val typeName = getterClass.name
                proxy!!.setFieldData(st, index++, value, typeName)
            }
        }

        if (update) {
            st.setInt(index, record.id)
        }
    }

    private fun getPreparedStatement(record: Record, conn: Connection, update: Boolean): PreparedStatement {
        val sql = if (update) getUpdateSQL(record) else getInsertSQL(record)
        val st = conn.prepareStatement(sql)
        setData(record, st, update)
        return st
    }

    private fun getDeleteStatement(record: Record, conn: Connection): PreparedStatement {
        val st = conn.prepareStatement(getDeleteSQL(record))
        st.setInt(1, record.id)
        return st
    }

    private fun getDeleteStatement(id: Int?, clazz: Class<out Record>, conn: Connection): PreparedStatement {
        val st = conn.prepareStatement(getDeleteSQL(clazz))
        st.setInt(1, id!!)
        return st
    }

    /**
     * Pre-loads necessary information about specified [list of tables][tables] from the just opened database.
     * This method must be called prior to any other database operations. Otherwise primary keys may be generated
     * incorrectly.
     */
    fun preload(tables: Collection<KClass<out Record>>) {
        // load primary key max values
        tables.filter { it.java.isAnnotationPresent(Table::class.java) }
                .forEach {
                    val a = it.java.getAnnotation(Table::class.java)
                    val id = getIdMaxValue(a.value)
                    primaryKeys.put(it.java, id)
                }
    }

    /**
     * Returns next available primary key value for the specified [class][clazz]. This method is thread safe.
     */
    fun generatePrimaryKey(clazz: KClass<out Record>): Int {
        return primaryKeys.compute(clazz.java) { _, v -> if (v == null) 1 else v + 1 }!!
    }

    private fun getIdMaxValue(tableName: String): Int {
        dataSource!!.connection.use { conn ->
            val st = conn.prepareStatement("SELECT id FROM $tableName ORDER BY id DESC")
            val rs = st.executeQuery()
            if (rs.next()) {
                return rs.getInt(1)
            } else {
                return 0
            }
        }
    }

    /**
     * Inserts new [record] with predefined id into the database. No attempt to generate
     * new id is made. Calling code must ensure that predefined id is unique.
     */
    fun <T : Record> insert(record: T): T? {
        Objects.requireNonNull(record.id)

        dataSource!!.connection.use {
            getPreparedStatement(record, it, false).use {
                it.executeUpdate()
                return get(record.id, record.javaClass)
            }
        }
    }

    /**
     * Inserts multiple [records] with predefined id using batch insert. No attempt to generate
     * new id is made. Calling code must ensure that predefined id is unique for all records.
     *
     * Supplied records are divided to batches of the specified [size]. To avoid memory issues [size] of the batch
     * must be tuned appropriately.
     */
    fun <T : Record> insert(size: Int, records: List<T>) {
        if (size < 1) {
            throw IllegalArgumentException("Batch size must be >= 1")
        }

        if (!records.isEmpty()) {
            val sql = getInsertSQL(records[0])

            connection.use { conn ->
                conn.prepareStatement(sql).use { st ->
                    var count = 0

                    for (r in records) {
                        setData(r, st, false)
                        st.addBatch()

                        if (++count % size == 0) {
                            st.executeBatch()
                        }
                    }

                    st.executeBatch()
                }
            }
        }
    }

    /**
     * Updates [record] in the database.
     */
    fun <T : Record> update(record: T): T? {
        Objects.requireNonNull(record.id)

        dataSource!!.connection.use {
            getPreparedStatement(record, it, true).use {
                it.executeUpdate()
                return get(record.id, record.javaClass)!!
            }
        }

    }

    /**
     * Deletes [record] from the database.
     */
    fun delete(record: Record) {
        dataSource!!.connection.use {
            conn -> getDeleteStatement(record, conn).use {
                ps -> ps.executeUpdate()
            }
        }
    }

    /**
     * Deletes record of the specified class from the database by [id].
     */
    fun delete(id: Int, clazz: KClass<out Record>) {
        dataSource!!.connection.use {
            conn -> getDeleteStatement(id, clazz.java, conn).use {
                ps -> ps.executeUpdate()
            }
        }
    }

    companion object {
        private val NOT_ANNOTATED = "Class is not properly annotated"
    }
}
