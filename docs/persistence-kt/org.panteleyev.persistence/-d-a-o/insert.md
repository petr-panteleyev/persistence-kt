[persistence-kt](../../index.md) / [org.panteleyev.persistence](../index.md) / [DAO](index.md) / [insert](.)

# insert

`fun <T : `[`Record`](../-record/index.md)`> insert(record: T): T?`

Inserts new [record](insert.md#org.panteleyev.persistence.DAO$insert(org.panteleyev.persistence.DAO.insert.T)/record) with predefined id into the database. No attempt to generate
new id is made. Calling code must ensure that predefined id is unique.

`fun <T : `[`Record`](../-record/index.md)`> insert(size: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, records: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Inserts multiple [records](insert.md#org.panteleyev.persistence.DAO$insert(kotlin.Int, kotlin.collections.List((org.panteleyev.persistence.DAO.insert.T)))/records) with predefined id using batch insert. No attempt to generate
new id is made. Calling code must ensure that predefined id is unique for all records.

Supplied records are divided to batches of the specified [size](insert.md#org.panteleyev.persistence.DAO$insert(kotlin.Int, kotlin.collections.List((org.panteleyev.persistence.DAO.insert.T)))/size). To avoid memory issues [size](insert.md#org.panteleyev.persistence.DAO$insert(kotlin.Int, kotlin.collections.List((org.panteleyev.persistence.DAO.insert.T)))/size) of the batch
must be tuned appropriately.

