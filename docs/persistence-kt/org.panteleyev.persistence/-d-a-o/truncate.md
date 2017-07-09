[persistence-kt](../../index.md) / [org.panteleyev.persistence](../index.md) / [DAO](index.md) / [truncate](.)

# truncate

`fun truncate(classes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../-record/index.md)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Truncates tables removing all records. Primary key generation starts from 1 again. For MySQL this operation
uses `TRUNCATE TABLE table_name` command. As SQLite does not support this command `DELETE FROM table_name`
is used instead.

