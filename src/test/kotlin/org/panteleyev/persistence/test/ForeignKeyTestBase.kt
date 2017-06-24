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

import org.panteleyev.persistence.Record
import org.panteleyev.persistence.test.model.ChildTable
import org.panteleyev.persistence.test.model.ParentTable
import org.testng.Assert
import org.testng.annotations.Test
import java.util.Arrays
import java.util.UUID

open class ForeignKeyTestBase : Base() {

    private fun deleteForbidden(record: Record) {
        var exception = false
        try {
            dao.delete(record)
        } catch (ex: Exception) {
            exception = true
        }

        Assert.assertTrue(exception)
    }

    private fun updateForbidden(record: ParentTable) {
        var exception = false
        try {
            record.value = UUID.randomUUID().toString()
            dao.update(record)
        } catch (ex: Exception) {
            exception = true
        }

        Assert.assertTrue(exception)
    }

    @Test
    @Throws(Exception::class)
    fun testForeignKeyOnDelete() {
        val classes = Arrays.asList(ParentTable::class, ChildTable::class)

        dao.createTables(classes)
        dao.preload(classes)

        val cascade = ParentTable(dao.generatePrimaryKey(ParentTable::class),
                UUID.randomUUID().toString())
        dao.insert(cascade)

        val restrict = ParentTable(dao.generatePrimaryKey(ParentTable::class),
                UUID.randomUUID().toString())
        dao.insert(restrict)

        val setNull = ParentTable(dao.generatePrimaryKey(ParentTable::class),
                UUID.randomUUID().toString())
        dao.insert(setNull)

        val noAction = ParentTable(dao.generatePrimaryKey(ParentTable::class),
                UUID.randomUUID().toString())
        dao.insert(noAction)

        val none = ParentTable(dao.generatePrimaryKey(ParentTable::class),
                UUID.randomUUID().toString())
        dao.insert(none)

        val table = ChildTable(
                dao.generatePrimaryKey(ChildTable::class),
                setNull.value,
                cascade.value,
                restrict.value,
                noAction.value,
                none.value
        )
        dao.insert(table)

        deleteForbidden(none)
        deleteForbidden(noAction)
        deleteForbidden(restrict)

        // Set null
        dao.delete(setNull)
        val setNullCheck = dao.get(table.id, ChildTable::class)
        Assert.assertNotNull(setNullCheck)
        Assert.assertNull(setNullCheck?.nullValue)

        // Cascade
        dao.delete(cascade)
        val cascadeCheck = dao.get(table.id, ChildTable::class)
        Assert.assertNull(cascadeCheck)
    }

    @Test
    fun testForeignKeyOnUpdate() {
        val classes = Arrays.asList(ParentTable::class, ChildTable::class)

        dao.createTables(classes)
        dao.preload(classes)

        val cascade = ParentTable(dao.generatePrimaryKey(ParentTable::class),
                UUID.randomUUID().toString())
        dao.insert(cascade)

        val restrict = ParentTable(dao.generatePrimaryKey(ParentTable::class),
                UUID.randomUUID().toString())
        dao.insert(restrict)

        val setNull = ParentTable(dao.generatePrimaryKey(ParentTable::class),
                UUID.randomUUID().toString())
        dao.insert(setNull)

        val noAction = ParentTable(dao.generatePrimaryKey(ParentTable::class),
                UUID.randomUUID().toString())
        dao.insert(noAction)

        val none = ParentTable(dao.generatePrimaryKey(ParentTable::class),
                UUID.randomUUID().toString())
        dao.insert(none)

        val table = ChildTable(
                dao.generatePrimaryKey(ChildTable::class),
                setNull.value,
                cascade.value,
                restrict.value,
                noAction.value,
                none.value
        )
        dao.insert(table)

        updateForbidden(none)
        updateForbidden(noAction)
        updateForbidden(restrict)

        // Set null
        setNull.value = UUID.randomUUID().toString()
        dao.update(setNull)
        val setNullCheck = dao.get(table.id, ChildTable::class)
        Assert.assertNotNull(setNullCheck)
        Assert.assertNull(setNullCheck?.nullValue)

        // Cascade
        cascade.value = UUID.randomUUID().toString()
        dao.update(cascade)
        val cascadeCheck = dao.get(table.id, ChildTable::class)
        Assert.assertEquals(cascadeCheck?.cascadeValue, cascade.value)
    }

}
