## PersistenceKt

Kotlin version of annotation based wrapper for SQLite and MySQL.

**Note**: this library uses the same packages and classes as [java-persistence](https://github.com/petr-panteleyev/java-persistence)
which makes them mutually exclusive in class path.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.panteleyev/persistence-kt/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.panteleyev/persistence-kt/)

### Releases

https://oss.sonatype.org/content/repositories/releases/org/panteleyev/persistence-kt/

### API Documentation

https://petr-panteleyev.github.io/persistence-kt/persistence-kt

## Implementation Details

### Database

Database manipulation is beyond the scope of this API. Calling code must supply correct DataSource and ensure database
does exist and proper access control is established. 

### Mutable Objects

Mutable object must implement mutable properties annotated as shown below:

```
@Table("book")
class Book (
        @param:Field("id")
        @get:Field(value = "id", primaryKey = true)
        override var id: Integer
        
        @param:Field("title")
        @get:Field("title")
        var title: String
): Record
```

### Immutable Objects

Immutable objects are supported by annotation ```@RecordBuilder``` as shown below.

```
@Table("book")
class Book @RecordBuilder constructor (
        @param:Field("id")
        @get:Field(value = "id", primaryKey = true)
        override val id: Integer
        
        @param:Field("title")
        @get:Field("title")
        val title: String
): Record
```

### Data Types

The following data types are supported:

| Java | SQLite | MySQL | Comment |
|---|---|---|---|
|int<br>Integer|INTEGER|INTEGER| |
|long<br>Long|INTEGER|BIGINT| |
|bool<br>Boolean|BOOLEAN|BOOLEAN| |
|String|VARCHAR (`Field.length`)|VARCHAR (`Field.length`)| |
|[BigDecimal](https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html)|VARCHAR (`Field.precision` + 1)|DECIMAL (`Field.precision`, `Field.scale`)|MySQL representation does not guarantee that retrieved value will be equal to original one by means of `Object.equals(Object)`. Use `BigDecimal.compareTo(BigDecimal)` instead.|
|[Date](https://docs.oracle.com/javase/8/docs/api/java/util/Date.html)|INTEGER|BIGINT|Dates are stored as long using `Date.getTime()`|

### Indexes and Foreign Keys

#### Parent Table

```
@Table("parent_table")
data class ParentTable : Record {
    @get:Field("data")
    @get:Index("data", unique = true)
    var data: String?
}
```

This will produce the following SQL for indexed field:

```CREATE UNIQUE INDEX data ON parent_table(data)```

#### Child Table

```
@Table("child_table")
data class ChildTable : Record {
        // ...
        @get:Field("cascade_value")
        @get:ForeignKey(table = ParentTable::class, field = "data",
                onDelete = ReferenceOption.RESTRICT, onUpdate = ReferenceOption.CASCADE)
        val cascadeValue: String?
        // ...
}
```

This will produce the following SQL for the foreign key:

```CREATE FOREIGN KEY(parent_data) REFERENCES parent_table(data) ON DELETE RESTRICT ON UPDATE CASCADE```
