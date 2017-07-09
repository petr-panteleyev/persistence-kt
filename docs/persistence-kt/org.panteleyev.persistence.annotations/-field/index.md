[persistence-kt](../../index.md) / [org.panteleyev.persistence.annotations](../index.md) / [Field](.)

# Field

`@Target([AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER]) annotation class Field`

Defines database record field. Must be applied to getters. If Record uses [RecordBuilder](../-record-builder/index.md) annotated constructor
its parameters must be annotated by Field as well.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Field(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, nullable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, primaryKey: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, length: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 255, precision: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 15, scale: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 6)`<br>Defines database record field. Must be applied to getters. If Record uses [RecordBuilder](../-record-builder/index.md) annotated constructor its parameters must be annotated by Field as well. |

### Properties

| Name | Summary |
|---|---|
| [length](length.md) | `val length: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Defines length of the field. |
| [nullable](nullable.md) | `val nullable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Defines if the field can be NULL. |
| [precision](precision.md) | `val precision: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Defines PRECISION, applicable to numeric data types. |
| [primaryKey](primary-key.md) | `val primaryKey: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Defines if the field is a primary key. |
| [scale](scale.md) | `val scale: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Defines SCALE, applicable to numeric data types. |
| [value](value.md) | `val value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>SQL name of the field. |
