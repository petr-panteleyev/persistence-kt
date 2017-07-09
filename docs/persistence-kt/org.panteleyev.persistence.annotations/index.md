[persistence-kt](../index.md) / [org.panteleyev.persistence.annotations](.)

## Package org.panteleyev.persistence.annotations

### Types

| Name | Summary |
|---|---|
| [ReferenceOption](-reference-option/index.md) | `enum class ReferenceOption`<br>Foreign key reference option. |

### Annotations

| Name | Summary |
|---|---|
| [Field](-field/index.md) | `annotation class Field`<br>Defines database record field. Must be applied to getters. If Record uses [RecordBuilder](-record-builder/index.md) annotated constructor its parameters must be annotated by [Field](-field/index.md) as well. |
| [ForeignKey](-foreign-key/index.md) | `annotation class ForeignKey`<br>Defines foreign key. |
| [Index](-index/index.md) | `annotation class Index`<br>Defines index for the table field. |
| [RecordBuilder](-record-builder/index.md) | `annotation class RecordBuilder`<br>Defines constructor used for record retrieval. All parameters of such constructor must be annotated with [Field](-field/index.md) annotation. |
| [Table](-table/index.md) | `annotation class Table`<br>Defines database table. |
