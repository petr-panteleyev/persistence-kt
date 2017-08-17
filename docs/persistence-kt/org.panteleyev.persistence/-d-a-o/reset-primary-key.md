[persistence-kt](../../index.md) / [org.panteleyev.persistence](../index.md) / [DAO](index.md) / [resetPrimaryKey](.)

# resetPrimaryKey

`protected fun resetPrimaryKey(table: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../-record/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Resets primary key generation for the given [table](reset-primary-key.md#org.panteleyev.persistence.DAO$resetPrimaryKey(kotlin.reflect.KClass((org.panteleyev.persistence.Record)))/table). Next call to [generatePrimaryKey](generate-primary-key.md) will return 1. This
method should only be used in case of manual table truncate.

