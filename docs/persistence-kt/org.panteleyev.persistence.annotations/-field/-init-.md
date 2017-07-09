[persistence-kt](../../index.md) / [org.panteleyev.persistence.annotations](../index.md) / [Field](index.md) / [&lt;init&gt;](.)

# &lt;init&gt;

`Field(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, nullable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, primaryKey: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, length: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 255, precision: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 15, scale: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 6)`

Defines database record field. Must be applied to getters. If Record uses [RecordBuilder](../-record-builder/index.md) annotated constructor
its parameters must be annotated by [Field](index.md) as well.

