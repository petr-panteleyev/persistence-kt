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

package org.panteleyev.persistence.test.model

import org.panteleyev.persistence.Record
import org.panteleyev.persistence.annotations.Field
import org.panteleyev.persistence.annotations.Table
import java.util.Random

@Table("primitives_table")
data class RecordWithPrimitives constructor(
        @param:Field(value = "id")
        @get:Field(value = "id", primaryKey = true)
        override var id: Int,

        @param:Field("a")
        @get:Field("a")
        var a: Int,

        @param:Field("b")
        @get:Field("b")
        var b: Boolean,

        @param:Field("c")
        @get:Field("c")
        var c: Long
) : Record {

    constructor() : this(0, 0, false, 0L)

    companion object {

        fun newRecord(id: Int, random: Random): RecordWithPrimitives {
            return RecordWithPrimitives(
                    id,
                    random.nextInt(),
                    random.nextBoolean(),
                    random.nextLong()
            )
        }

        fun newNullRecord(id: Int): RecordWithPrimitives {
            return RecordWithPrimitives(id, 0, false, 0L)
        }
    }
}
