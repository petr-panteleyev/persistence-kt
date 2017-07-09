[persistence-kt](../../index.md) / [org.panteleyev.persistence](../index.md) / [DAO](.)

# DAO

`open class DAO`

Persistence API entry point.

### Parameters

`ds` - data source

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DAO(ds: `[`DataSource`](http://docs.oracle.com/javase/8/docs/api/javax/sql/DataSource.html)`?)`<br>Creates new DAO object with optional data source. |

### Properties

| Name | Summary |
|---|---|
| [connection](connection.md) | `val connection: `[`Connection`](http://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html)<br>New connection returned by internal data source. |
| [dataSource](data-source.md) | `var dataSource: `[`DataSource`](http://docs.oracle.com/javase/8/docs/api/javax/sql/DataSource.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [createTables](create-tables.md) | `fun createTables(tables: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../-record/index.md)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Creates tables of the [specified classes](create-tables.md#org.panteleyev.persistence.DAO$createTables(kotlin.collections.List((kotlin.reflect.KClass((org.panteleyev.persistence.Record)))))/tables) according to their annotations. |
| [delete](delete.md) | `fun delete(record: `[`Record`](../-record/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Deletes [record](delete.md#org.panteleyev.persistence.DAO$delete(org.panteleyev.persistence.Record)/record) from the database.`fun delete(id: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../-record/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Deletes record of the specified class from the database by [id](delete.md#org.panteleyev.persistence.DAO$delete(kotlin.Int, kotlin.reflect.KClass((org.panteleyev.persistence.Record)))/id). |
| [generatePrimaryKey](generate-primary-key.md) | `fun generatePrimaryKey(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../-record/index.md)`>): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns next available primary key value for the specified [class](generate-primary-key.md#org.panteleyev.persistence.DAO$generatePrimaryKey(kotlin.reflect.KClass((org.panteleyev.persistence.Record)))/clazz). This method is thread safe. |
| [get](get.md) | `fun <T : `[`Record`](../-record/index.md)`> get(id: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T?`<br>Retrieves record from the database using record [id](get.md#org.panteleyev.persistence.DAO$get(kotlin.Int, kotlin.reflect.KClass((org.panteleyev.persistence.DAO.get.T)))/id). |
| [getAll](get-all.md) | `fun <T : `[`Record`](../-record/index.md)`> getAll(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>`<br>Retrieves all records of the specified [class](get-all.md#org.panteleyev.persistence.DAO$getAll(kotlin.reflect.KClass((org.panteleyev.persistence.DAO.getAll.T)))/clazz).`fun <T : `[`Record`](../-record/index.md)`> getAll(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>, map: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Retrieves all records of the specified [class](get-all.md#org.panteleyev.persistence.DAO$getAll(kotlin.reflect.KClass((org.panteleyev.persistence.DAO.getAll.T)), kotlin.collections.MutableMap((kotlin.Int, org.panteleyev.persistence.DAO.getAll.T)))/clazz) and fills the [map](get-all.md#org.panteleyev.persistence.DAO$getAll(kotlin.reflect.KClass((org.panteleyev.persistence.DAO.getAll.T)), kotlin.collections.MutableMap((kotlin.Int, org.panteleyev.persistence.DAO.getAll.T)))/map). |
| [insert](insert.md) | `fun <T : `[`Record`](../-record/index.md)`> insert(record: T): T?`<br>Inserts new [record](insert.md#org.panteleyev.persistence.DAO$insert(org.panteleyev.persistence.DAO.insert.T)/record) with predefined id into the database. No attempt to generate new id is made. Calling code must ensure that predefined id is unique.`fun <T : `[`Record`](../-record/index.md)`> insert(size: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, records: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Inserts multiple [records](insert.md#org.panteleyev.persistence.DAO$insert(kotlin.Int, kotlin.collections.List((org.panteleyev.persistence.DAO.insert.T)))/records) with predefined id using batch insert. No attempt to generate new id is made. Calling code must ensure that predefined id is unique for all records. |
| [preload](preload.md) | `fun preload(tables: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../-record/index.md)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Pre-loads necessary information about specified [list of tables](preload.md#org.panteleyev.persistence.DAO$preload(kotlin.collections.Collection((kotlin.reflect.KClass((org.panteleyev.persistence.Record)))))/tables) from the just opened database. This method must be called prior to any other database operations. Otherwise primary keys may be generated incorrectly. |
| [truncate](truncate.md) | `fun truncate(classes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Record`](../-record/index.md)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Truncates tables removing all records. Primary key generation starts from 1 again. For MySQL this operation uses `TRUNCATE TABLE table_name` command. As SQLite does not support this command `DELETE FROM table_name` is used instead. |
| [update](update.md) | `fun <T : `[`Record`](../-record/index.md)`> update(record: T): T?`<br>Updates [record](update.md#org.panteleyev.persistence.DAO$update(org.panteleyev.persistence.DAO.update.T)/record) in the database. |
