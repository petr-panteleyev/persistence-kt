[persistence-kt](../../index.md) / [org.panteleyev.persistence](../index.md) / [Record](.)

# Record

`interface Record`

Database record.

### Properties

| Name | Summary |
|---|---|
| [id](id.md) | `abstract val id: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>ID of the record. Default implementation does not provide setter to support immutable objects. Mutable records must override this property as var. |
