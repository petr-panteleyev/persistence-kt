[persistence-kt](../../index.md) / [org.panteleyev.persistence.annotations](../index.md) / [ReferenceOption](.)

# ReferenceOption

`enum class ReferenceOption`

Foreign key reference option.

### Enum Values

| Name | Summary |
|---|---|
| [NONE](-n-o-n-e.md) | No action is specified in the ON DELETE or ON UPDATE clause. |
| [RESTRICT](-r-e-s-t-r-i-c-t.md) | Rejects the delete or update operation for the parent table. |
| [CASCADE](-c-a-s-c-a-d-e.md) | Delete or update the row from the parent table, and automatically delete or update the matching rows in the child table. |
| [SET_NULL](-s-e-t_-n-u-l-l.md) | Delete or update the row from the parent table, and set the foreign key column or columns in the child table to NULL. |
| [NO_ACTION](-n-o_-a-c-t-i-o-n.md) | Same as [RESTRICT](-r-e-s-t-r-i-c-t.md) |

### Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns SQL representation of the reference option. |
