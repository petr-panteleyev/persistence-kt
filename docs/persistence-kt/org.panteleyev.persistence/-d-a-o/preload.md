[persistence-kt](../../index.md) / [org.panteleyev.persistence](../index.md) / [DAO](index.md) / [preload](.)

# preload

`fun preload(tables: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../-record/index.md)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Pre-loads necessary information about specified [list of tables](preload.md#org.panteleyev.persistence.DAO$preload(kotlin.collections.Collection((kotlin.reflect.KClass((org.panteleyev.persistence.Record)))))/tables) from the just opened database.
This method must be called prior to any other database operations. Otherwise primary keys may be generated
incorrectly.

