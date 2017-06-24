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

package org.panteleyev.persistence.test

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import org.panteleyev.persistence.DAO
import org.panteleyev.persistence.Record
import org.panteleyev.persistence.test.model.ImmutableRecord
import org.panteleyev.persistence.test.model.RecordWithAllTypes
import org.panteleyev.persistence.test.model.RecordWithPrimitives
import org.testng.SkipException
import org.testng.annotations.DataProvider
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.sql.SQLException
import java.util.Random
import javax.sql.DataSource
import kotlin.reflect.KClass

open class Base {

    val dao: DAO by lazy {
        DAO(dataSource!!)
    }

    private var dataSource: DataSource? = null

    // SQLite
    private var TEST_DB_FILE: File? = null

    internal fun setupMySQL() {
        val dbName = System.getProperty("mysql.database", TEST_DB_NAME)
        val host = System.getProperty("mysql.host", "localhost")
        val user = System.getProperty("mysql.user")
        val password = System.getProperty("mysql.password")

        if (user == null || password == null) {
            throw SkipException("Test config is not set")
        }

        dataSource = MySQLBuilder()
                .host(host)
                .user(user)
                .password(password)
                .build()

        try {
            dataSource!!.connection.use { conn ->
                val st = conn.createStatement()
                st.execute("CREATE DATABASE " + dbName)
                (dataSource as MysqlDataSource).databaseName = dbName
            }
        } catch (ex: SQLException) {
            throw SkipException("Unable to create database", ex)
        }

    }

    internal fun cleanupMySQL() {
        dataSource!!.connection.use { conn ->
            val st = conn.createStatement()
            st.execute("DROP DATABASE " + TEST_DB_NAME)
        }
    }

    internal fun setupSQLite() {
        try {
            TEST_DB_FILE = File.createTempFile("persistence", "db")

            dataSource = SQLiteBuilder()
                    .file(TEST_DB_FILE!!.absolutePath)
                    .build()
        } catch (ex: IOException) {
            throw SkipException("Unable to create temporary file", ex)
        }

    }

    internal fun cleanupSQLite() {
        if (TEST_DB_FILE != null && TEST_DB_FILE!!.exists()) {
            TEST_DB_FILE!!.delete()
        }
    }

    @DataProvider(name = "recordClasses")
    fun recordClassesProvider(): Array<Array<Any>> {
        return arrayOf(
                arrayOf<Any>(RecordWithAllTypes::class),
                arrayOf<Any>(ImmutableRecord::class)
        )
    }

    protected fun givenRandomRecord(clazz: KClass<out Record>): Record {
        val id = dao.generatePrimaryKey(clazz)
        return givenRandomRecordWithId(clazz, id)
    }

    protected fun givenRandomRecordWithId(clazz: KClass<out Record>, id: Int): Record {
        if (clazz == RecordWithAllTypes::class) {
            return RecordWithAllTypes.newRecord(id, RANDOM)
        }

        if (clazz == RecordWithPrimitives::class) {
            return RecordWithAllTypes.newRecord(id, RANDOM)
        }

        if (clazz == ImmutableRecord::class) {
            return ImmutableRecord.newRecord(id, RANDOM)
        }

        throw IllegalStateException("No class")
    }

    protected fun givenNullRecord(clazz: KClass<out Record>): Record {
        val id = dao.generatePrimaryKey(clazz)

        if (clazz == RecordWithAllTypes::class) {
            return RecordWithAllTypes.newNullRecord(id)
        }

        if (clazz == RecordWithPrimitives::class) {
            return RecordWithAllTypes.newNullRecord(id)
        }

        if (clazz == ImmutableRecord::class) {
            return ImmutableRecord.newNullRecord(id)
        }

        throw IllegalStateException("No class")
    }

    companion object {
        internal val RANDOM = Random(System.currentTimeMillis())

        // MySQL
        private val TEST_DB_NAME = "TestDB"

        fun compareBigDecimals(x: BigDecimal?, y: BigDecimal?): Boolean {
            return x == y || x != null && x.compareTo(y) == 0
        }
    }
}