[persistence-kt](../../index.md) / [org.panteleyev.persistence](../index.md) / [Record](index.md) / [id](.)

# id

`abstract val id: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

ID of the record. Default implementation does not provide setter to support
immutable objects. Mutable records must override this property as var.

