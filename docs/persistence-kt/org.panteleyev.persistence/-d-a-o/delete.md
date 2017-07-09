[persistence-kt](../../index.md) / [org.panteleyev.persistence](../index.md) / [DAO](index.md) / [delete](.)

# delete

`fun delete(record: `[`Record`](../-record/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Deletes [record](delete.md#org.panteleyev.persistence.DAO$delete(org.panteleyev.persistence.Record)/record) from the database.

`fun delete(id: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../-record/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Deletes record of the specified class from the database by [id](delete.md#org.panteleyev.persistence.DAO$delete(kotlin.Int, kotlin.reflect.KClass((org.panteleyev.persistence.Record)))/id).

