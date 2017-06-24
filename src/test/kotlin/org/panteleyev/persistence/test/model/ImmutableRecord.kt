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
import org.panteleyev.persistence.annotations.RecordBuilder
import org.panteleyev.persistence.annotations.Table
import org.panteleyev.persistence.test.Base
import java.math.BigDecimal
import java.util.Date
import java.util.Objects
import java.util.Random
import java.util.UUID

@Table("immutable_table")
data class ImmutableRecord @RecordBuilder constructor(
        @param:Field("id")
        @get:Field(value = "id", primaryKey = true)
        override val id: Int,

        // fields
        @param:Field("a")
        @get:Field("a")
        val a: String?,

        @param:Field("b")
        @get:Field("b")
        val b: Int?,

        @param:Field("c")
        @get:Field("c")
        val c: Boolean?,

        @param:Field("d")
        @get:Field("d")
        val d: Date?,

        @param:Field("e")
        @get:Field("e")
        val e: Long?,

        @param:Field("f")
        @get:Field("f")
        val f: BigDecimal?
) : Record {

    override fun equals(other: Any?): Boolean {
        if (other is ImmutableRecord) {
            return this.id == other.id
                    && this.a == other.a
                    && this.b == other.b
                    && this.c == other.c
                    && this.d == other.d
                    && this.e == other.e
                    && Base.compareBigDecimals(this.f, other.f)
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(a, b, c, d, e, f?.stripTrailingZeros())
    }

    companion object {
        fun newRecord(id: Int, random: Random): ImmutableRecord {
            return ImmutableRecord(
                    id = id,
                    a = UUID.randomUUID().toString(),
                    b = random.nextInt(),
                    c = random.nextBoolean(),
                    d = Date(),
                    e = random.nextLong(),
                    f = BigDecimal.TEN
            )
        }

        fun newNullRecord(id: Int): ImmutableRecord {
            return ImmutableRecord(id, null, null, null, null, null, null)
        }
    }
}
