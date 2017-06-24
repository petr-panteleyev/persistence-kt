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

package org.panteleyev.persistence.annotations

import org.panteleyev.persistence.Record
import kotlin.reflect.KClass

/**
 * Defines database table.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Table(
        /**
         * SQL name of the table.
         */
        val value: String
)

/**
 * Defines database record field. Must be applied to getters. If Record uses [RecordBuilder] annotated constructor
 * its parameters must be annotated by [Field] as well.
 *
 * @property value SQL name of the field.
 * @property nullable Defines if the field can be NULL.
 * @property primaryKey Defines if the field is a primary key.
 * @property length Defines length of the field.
 * @property precision Defines PRECISION, applicable to numeric data types.
 * @property scale Defines SCALE, applicable to numeric data types.
 */
@Target(AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field (
        val value: String,
        val nullable: Boolean = true,
        val primaryKey: Boolean = false,
        val length: Int = 255,
        val precision: Int = 15,
        val scale: Int = 6
)

/**
 * Defines foreign key.
 *
 * @property table Referenced table class. This must be a class annotated by [Table].
 * @property field Referenced field.
 * @property onDelete ON DELETE reference option.
 * @property onUpdate ON UPDATE reference option.
 */
@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ForeignKey (
        val table: KClass<out Record>,
        val field: String = "id",
        val onDelete: ReferenceOption = ReferenceOption.NONE,
        val onUpdate: ReferenceOption = ReferenceOption.NONE
)

/**
 * Defines index for the table field.
 *
 * @property value Name of the index.
 * @property unique Specifies whether this index must be unique.
 */
@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Index(
        val value: String,
        val unique: Boolean = false
)

/**
 * Defines constructor used for record retrieval. All parameters of such constructor must be annotated with
 * [Field] annotation.
 */
@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
annotation class RecordBuilder