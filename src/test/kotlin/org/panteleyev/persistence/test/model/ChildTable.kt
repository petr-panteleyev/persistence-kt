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
import org.panteleyev.persistence.annotations.ForeignKey
import org.panteleyev.persistence.annotations.RecordBuilder
import org.panteleyev.persistence.annotations.ReferenceOption
import org.panteleyev.persistence.annotations.Table

@Table("child_table")
data class ChildTable @RecordBuilder constructor(
        @param:Field("id")
        @get:Field("id", primaryKey = true)
        override val id: Int,

        @param:Field("null_value")
        @get:Field("null_value")
        @get:ForeignKey(table = ParentTable::class, field = "value",
                onDelete = ReferenceOption.SET_NULL, onUpdate = ReferenceOption.SET_NULL)
        val nullValue: String?,

        @param:Field("cascade_value")
        @get:Field("cascade_value")
        @get:ForeignKey(table = ParentTable::class, field = "value",
                onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
        val cascadeValue: String?,

        @param:Field("restrict_value")
        @get:Field("restrict_value")
        @get:ForeignKey(table = ParentTable::class, field = "value",
                onDelete = ReferenceOption.RESTRICT, onUpdate = ReferenceOption.RESTRICT)
        val restrictValue: String?,

        @param:Field("no_action_value")
        @get:Field("no_action_value")
        @get:ForeignKey(table = ParentTable::class, field = "value",
                onDelete = ReferenceOption.NO_ACTION, onUpdate = ReferenceOption.NO_ACTION)
        val noActionValue: String?,

        @param:Field("none_value")
        @get:Field("none_value")
        @get:ForeignKey(table = ParentTable::class, field = "value")
        val noneValue: String?
) : Record
