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
import org.panteleyev.persistence.DAOTypes.TYPE_BIG_DECIMAL
import org.panteleyev.persistence.DAOTypes.TYPE_BOOL
import org.panteleyev.persistence.DAOTypes.TYPE_BOOLEAN
import org.panteleyev.persistence.DAOTypes.TYPE_DATE
import org.panteleyev.persistence.DAOTypes.TYPE_INT
import org.panteleyev.persistence.DAOTypes.TYPE_INTEGER
import org.panteleyev.persistence.DAOTypes.TYPE_LONG
import org.panteleyev.persistence.DAOTypes.TYPE_LONG_PRIM
import org.panteleyev.persistence.DAOTypes.TYPE_STRING
import org.panteleyev.persistence.annotations.Field
import org.panteleyev.persistence.annotations.ForeignKey
import org.panteleyev.persistence.annotations.Index
import org.panteleyev.persistence.annotations.ReferenceOption
import org.panteleyev.persistence.annotations.Table
import java.lang.reflect.Method
import java.math.BigDecimal
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import java.util.Date
import java.util.Objects
import kotlin.reflect.KClass

internal interface DAOProxy {
    fun getFieldValue(fieldName: String, typeClass: Class<*>, set: ResultSet): Any?

    fun getColumnString(fld: Field, foreignKey: ForeignKey?, typeName: String, constraints: MutableList<String>): String

    fun truncate(conn: Connection, classes: List<KClass<out Record>>)

    fun setFieldData(st: PreparedStatement, index: Int, value: Any?, typeName: String) {
        when (typeName) {
            TYPE_STRING -> if (value == null) {
                st.setNull(index, Types.VARCHAR)
            } else {
                st.setString(index, value as String?)
            }
            TYPE_BOOL, TYPE_BOOLEAN -> if (value == null) {
                st.setNull(index, Types.BOOLEAN)
            } else {
                st.setBoolean(index, (value as Boolean?)!!)
            }
            TYPE_INTEGER, TYPE_INT -> if (value == null) {
                st.setNull(index, Types.INTEGER)
            } else {
                st.setInt(index, (value as Int?)!!)
            }
            TYPE_LONG, TYPE_LONG_PRIM -> if (value == null) {
                st.setNull(index, Types.INTEGER)
            } else {
                st.setLong(index, (value as Long?)!!)
            }
            TYPE_DATE -> if (value == null) {
                st.setNull(index, Types.INTEGER)
            } else {
                st.setLong(index, (value as Date).time)
            }
            TYPE_BIG_DECIMAL -> if (value == null) {
                st.setNull(index, Types.DECIMAL)
            } else {
                st.setBigDecimal(index, value as BigDecimal?)
            }
            else -> throw IllegalStateException(BAD_FIELD_TYPE)
        }
    }

    fun buildForeignKey(field: Field, key: ForeignKey): String {
        Objects.requireNonNull(key)

        val parentTableClass = key.table.java
        if (!parentTableClass.isAnnotationPresent(Table::class.java)) {
            throw IllegalStateException("Foreign key references not annotated table")
        }

        val parentTableName = parentTableClass.getAnnotation(Table::class.java).value
        val parentFieldName = key.field

        val fk = StringBuilder()

        fk.append("FOREIGN KEY (${field.value}) REFERENCES $parentTableName ($parentFieldName)")

        if (key.onUpdate != ReferenceOption.NONE) {
            fk.append(" ON UPDATE ${key.onUpdate}")
        }

        if (key.onDelete != ReferenceOption.NONE) {
            fk.append(" ON DELETE ${key.onDelete}")
        }

        return fk.toString()
    }

    fun buildIndex(table: Table, getter: Method): String {
        val field = getter.getAnnotation(Field::class.java)
        val index = getter.getAnnotation(Index::class.java)

        val b = StringBuilder("CREATE ")
        if (index.unique) {
            b.append("UNIQUE ")
        }

        b.append("INDEX ${index.value} ON ${table.value} (${field.value})")

        return b.toString()
    }
}