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
import javax.sql.DataSource

internal class MySQLBuilder {
    private var host: String? = null
    private var port = 3306
    private var dbName: String? = null
    private var user: String? = null
    private var password: String? = null

    fun build(): DataSource {
        val ds = MysqlDataSource()
        ds.databaseName = dbName
        ds.port = port
        ds.serverName = host
        ds.user = user
        ds.setPassword(password)
        return ds
    }

    fun host(host: String): MySQLBuilder {
        this.host = host
        return this
    }

    fun port(port: Int): MySQLBuilder {
        this.port = port
        return this
    }

    fun name(name: String): MySQLBuilder {
        this.dbName = name
        return this
    }

    fun user(user: String): MySQLBuilder {
        this.user = user
        return this
    }

    fun password(password: String): MySQLBuilder {
        this.password = password
        return this
    }
}
