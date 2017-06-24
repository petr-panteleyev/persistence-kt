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
import org.panteleyev.persistence.test.model.RecordWithAllTypes
import org.panteleyev.persistence.test.model.RecordWithPrimitives
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import kotlin.reflect.KClass

open class RecordsTestBase : Base() {

    @Test(dataProvider = "recordClasses")
    fun testRecordCreation(clazz: KClass<out Record>) {
        dao.createTables(listOf(clazz))
        dao.preload(listOf(clazz))

        val idMap = HashMap<Int, Record>()

        // Create all new records
        for (i in 0..RECORD_COUNT_1 - 1) {
            val newRecord = givenRandomRecord(clazz)
            dao.insert(newRecord)
            val result = dao.get(newRecord.id, clazz)!!
            idMap.put(result.id, result)
        }

        checkCreatedRecord(clazz, idMap, RECORD_COUNT_1)
    }

    @Test(dataProvider = "recordClasses")
    fun testRecordPutGet(clazz: KClass<out Record>) {
        dao.createTables(listOf(clazz))
        dao.preload(listOf(clazz))

        for (i in 0..RECORD_COUNT_1 - 1) {
            val record = givenRandomRecord(clazz)

            dao.insert(record)
            val result = dao.get(record.id, clazz)
            Assert.assertEquals(result, record)
        }
    }

    @Test(dataProvider = "recordClasses")
    fun testRecordPutDelete(clazz: KClass<out Record>) {
        dao.createTables(listOf(clazz))
        dao.preload(listOf(clazz))

        // Delete by record
        for (i in 0..RECORD_COUNT_1 - 1) {
            val record = givenRandomRecord(clazz)
            dao.insert(record)
            var result = dao.get(record.id, clazz)
            Assert.assertEquals(result, record)

            dao.delete(record)
            result = dao.get(record.id, clazz)
            Assert.assertNull(result)
        }

        // Delete by id
        for (i in 0..RECORD_COUNT_1 - 1) {
            val record = givenRandomRecord(clazz)
            dao.insert(record)
            var result = dao.get(record.id, clazz)
            Assert.assertEquals(result, record)

            dao.delete(record.id, clazz)
            result = dao.get(record.id, clazz)
            Assert.assertNull(result)
        }
    }

    @Test(dataProvider = "recordClasses")
    fun testRecordUpdate(clazz: KClass<out Record>) {
        dao.createTables(listOf(clazz))
        dao.preload(listOf(clazz))

        val original = givenRandomRecord(clazz)

        dao.insert(original)

        val updated = givenRandomRecordWithId(clazz, original.id)

        dao.update(updated)
        val updateResult = dao.get(updated.id, clazz)
        Assert.assertEquals(updateResult?.id?:0, original.id)

        val retrievedUpdated = dao.get(original.id, clazz)
        Assert.assertEquals(retrievedUpdated, updateResult)
        Assert.assertEquals(retrievedUpdated, updated)
    }

    @Test(dataProvider = "recordClasses")
    fun testNullFields(clazz: KClass<out Record>) {
        dao.createTables(listOf(clazz))
        dao.preload(listOf(clazz))

        val record = givenNullRecord(clazz)

        dao.insert(record)
        val insertResult = dao.get(record.id, clazz)
        Assert.assertEquals(insertResult, record)

        val retrieved = dao.get(record.id, clazz)
        Assert.assertEquals(retrieved, record)
    }

    @Test
    fun testExtremeValues() {
        val clazz = RecordWithPrimitives::class

        dao.createTables(listOf(clazz))
        dao.preload(listOf(clazz))

        // Max values
        val idMax = dao.generatePrimaryKey(clazz)
        val rMax = RecordWithPrimitives(idMax,
                Integer.MAX_VALUE,
                RANDOM.nextBoolean(),
                Long.MAX_VALUE)
        dao.insert(rMax)
        val insertResultMax = dao.get(rMax.id, clazz)
        Assert.assertEquals(insertResultMax, rMax)

        val retrievedMax = dao.get(rMax.id, clazz)
        Assert.assertEquals(retrievedMax, rMax)

        // Min values
        val idMin = dao.generatePrimaryKey(clazz)
        val rMin = RecordWithPrimitives(idMin,
                Integer.MIN_VALUE,
                RANDOM.nextBoolean(),
                Long.MIN_VALUE)
        dao.insert(rMin)
        val insertResultMin = dao.get(rMin.id, clazz)
        Assert.assertEquals(insertResultMin, rMin)

        val retrievedMin = dao.get(rMin.id, clazz)
        Assert.assertEquals(retrievedMin, rMin)
    }

    @DataProvider(name = "testBatchInsert")
    fun testBatchInsertDataProvider(): Array<Array<Any>> {
        return arrayOf(arrayOf<Any>(100, 7), arrayOf<Any>(100, 10))
    }

    @Test(dataProvider = "testBatchInsert")
    fun testBatchInsert(count: Int, batchSize: Int) {
        val clazz = RecordWithPrimitives::class

        dao.createTables(listOf(clazz))
        dao.preload(listOf(clazz))

        // Create records
        val records = mutableListOf<Record>()
        for (i in 0..count - 1) {
            records.add(RecordWithPrimitives.newRecord(dao.generatePrimaryKey(clazz), RANDOM))
        }

        // Insert records
        dao.insert(batchSize, records)

        // Retrieve records
        val retrieved = dao.getAll(clazz)

        // Size must be the same
        Assert.assertEquals(retrieved.size, records.size)
    }

    private fun <T : Record> checkCreatedRecord(clazz: KClass<T>, idMap: Map<Int, Record>, count: Int) {
        // Get all records back in one request
        val result = dao.getAll(clazz)

        // Check total amount or records returned
        Assert.assertEquals(result.size, count)
        Assert.assertEquals(result.size, idMap.keys.size)

        // Check uniqueness of all primary keys
        Assert.assertEquals(result
                .map({ it.id })
                .distinct()
                .count(), count)
    }

    companion object {
        private val RECORD_COUNT_1 = 10
        private val RECORD_COUNT_2 = 10

        private val ALL_CLASSES = listOf(RecordWithAllTypes::class, RecordWithAllTypes::class)
    }
}
