# module persistence-kotlin

Persistence library provides annotation classes for database access.

Database manipulation is beyond the scope of this API. Calling code must supply correct DataSource and ensure 
database does exist and proper access control is established.

# package org.panteleyev.persistence.kt

This package defines persistence API.

# package org.panteleyev.persistence.kt.annotations

This package defines annotations applied to Java classes implementing database table records.

## Table

Class implementing database table is defined by the annotation [@Table][org.panteleyev.persistence.kt.annotations.Table]. 
Such class must also implement interface [Record][org.panteleyev.persistence.Record].

API currently supports only integer as primary key type. Use [DAO.generatePrimaryKey][org.panteleyev.persistence.DAO.generatePrimaryKey] 
to generate unique values for each table class. Also make sure that application calls [DAO.preload][org.panteleyev.persistence.DAO.preload]
first.


## Indexes and Foreign Keys

### Parent Table

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

### Child Table

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
