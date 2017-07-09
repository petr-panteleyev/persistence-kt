[persistence-kt](../../index.md) / [org.panteleyev.persistence.annotations](../index.md) / [ForeignKey](.)

# ForeignKey

`@Target([AnnotationTarget.PROPERTY_GETTER]) annotation class ForeignKey`

Defines foreign key.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ForeignKey(table: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../../org.panteleyev.persistence/-record/index.md)`>, field: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "id", onDelete: `[`ReferenceOption`](../-reference-option/index.md)` = ReferenceOption.NONE, onUpdate: `[`ReferenceOption`](../-reference-option/index.md)` = ReferenceOption.NONE)`<br>Defines foreign key. |

### Properties

| Name | Summary |
|---|---|
| [field](field.md) | `val field: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Referenced field. |
| [onDelete](on-delete.md) | `val onDelete: `[`ReferenceOption`](../-reference-option/index.md)<br>ON DELETE reference option. |
| [onUpdate](on-update.md) | `val onUpdate: `[`ReferenceOption`](../-reference-option/index.md)<br>ON UPDATE reference option. |
| [table](table.md) | `val table: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../../org.panteleyev.persistence/-record/index.md)`>`<br>Referenced table class. This must be a class annotated by [Table](../-table/index.md). |
