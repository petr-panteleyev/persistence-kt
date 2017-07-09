[persistence-kt](../../index.md) / [org.panteleyev.persistence](../index.md) / [DAO](index.md) / [getAll](.)

# getAll

`fun <T : `[`Record`](../-record/index.md)`> getAll(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>`

Retrieves all records of the specified [class](get-all.md#org.panteleyev.persistence.DAO$getAll(kotlin.reflect.KClass((org.panteleyev.persistence.DAO.getAll.T)))/clazz).

`fun <T : `[`Record`](../-record/index.md)`> getAll(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>, map: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Retrieves all records of the specified [class](get-all.md#org.panteleyev.persistence.DAO$getAll(kotlin.reflect.KClass((org.panteleyev.persistence.DAO.getAll.T)), kotlin.collections.MutableMap((kotlin.Int, org.panteleyev.persistence.DAO.getAll.T)))/clazz) and fills the [map](get-all.md#org.panteleyev.persistence.DAO$getAll(kotlin.reflect.KClass((org.panteleyev.persistence.DAO.getAll.T)), kotlin.collections.MutableMap((kotlin.Int, org.panteleyev.persistence.DAO.getAll.T)))/map).

