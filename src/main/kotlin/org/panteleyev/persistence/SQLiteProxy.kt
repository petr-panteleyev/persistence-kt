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

import org.panteleyev.persistence.annotations.Field
import org.panteleyev.persistence.annotations.ForeignKey
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Date
import kotlin.reflect.KClass

internal class SQLiteProxy : DAOProxy {

    @Throws(SQLException::class)
    override fun getFieldValue(fieldName: String, typeClass: Class<*>, set: ResultSet): Any? {
        var value: Any? = set.getObject(fieldName)

        val typeName = typeClass.name

        if (value != null) {
            when (typeName) {
                DAOTypes.TYPE_STRING,
                DAOTypes.TYPE_INTEGER,
                DAOTypes.TYPE_INT,
                DAOTypes.TYPE_LONG,
                DAOTypes.TYPE_LONG_PRIM -> {
                    // do nothing
                }

                DAOTypes.TYPE_BOOL,
                DAOTypes.TYPE_BOOLEAN ->
                    value = value as Int != 0

                DAOTypes.TYPE_BIG_DECIMAL ->
                    value = set.getBigDecimal(fieldName)

                DAOTypes.TYPE_DATE ->
                    value = Date(set.getLong(fieldName))

                else -> throw IllegalStateException(DAOTypes.BAD_FIELD_TYPE)
            }// do nothing
        } else {
            when (typeName) {
                DAOTypes.TYPE_INT -> value = 0
                DAOTypes.TYPE_LONG_PRIM -> value = 0L
                DAOTypes.TYPE_BOOL -> value = false
            }
        }

        return value
    }

    override fun getColumnString(fld: Field, foreignKey: ForeignKey?, typeName: String, constraints: MutableList<String>): String {
        val b = StringBuilder()

        when (typeName) {
            DAOTypes.TYPE_STRING ->
                b.append("VARCHAR(${fld.length})")

            DAOTypes.TYPE_BOOL,
            DAOTypes.TYPE_BOOLEAN ->
                b.append("BOOLEAN")

            DAOTypes.TYPE_INTEGER,
            DAOTypes.TYPE_INT,
            DAOTypes.TYPE_LONG,
            DAOTypes.TYPE_LONG_PRIM,
            DAOTypes.TYPE_DATE ->
                b.append("INTEGER")

            DAOTypes.TYPE_BIG_DECIMAL ->
                b.append("VARCHAR(${fld.precision + 1})")

            else -> throw IllegalStateException(DAOTypes.BAD_FIELD_TYPE)
        }

        if (fld.primaryKey) {
            b.append(" PRIMARY KEY")
        }

        if (!fld.nullable) {
            b.append(" NOT NULL")
        }

        if (foreignKey != null) {
            constraints.add(buildForeignKey(fld, foreignKey))
        }

        return b.toString()
    }

    override fun truncate(conn: Connection, classes: List<KClass<out Record>>) {
        conn.createStatement().let { st ->
            classes.forEach { st.execute("DELETE FROM ${it.getTableName()}") }
            st.execute("VACUUM")
        }
    }
}
