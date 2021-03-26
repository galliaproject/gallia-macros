<p align="center"><img src="./images/logo.png" alt="icon"></p>

See original announcement on the _Scala_ [user list](https://users.scala-lang.org/t/introducing-gallia-a-library-for-data-manipulation/7112/11). For more information, see gallia-core [documentation](https://github.com/galliaproject/gallia-core/blob/master/README.md#introducing-gallia-a-scala-library-for-data-manipulation), in particular the [Macros section](https://github.com/galliaproject/gallia-core/blob/master/README.md#macros).


<a name="description"></a>
### Description

Allows the following:

```scala
import aptus._
import gallia._
import gallia.macros._

val johnHead: HeadO = johnDynamic // for readability

// ----------------------------------------------------------------------
schematizeRuntime[Person].p

// ======================================================================
assert(  toHeadMacro        (johnStatic).forceAObj == johnHead.forceAObj)
assert(fromHeadMacro[Person](johnHead)             == johnStatic)

// ----------------------------------------------------------------------
assert(  toAObjMacro        (johnStatic)           == johnDynamic)
assert(fromAObjMacro[Person](johnDynamic)          == johnStatic)

// ----------------------------------------------------------------------
// data only
assert(  toObjMacro         (johnStatic)           == johnDynamic.data)
assert(fromObjMacro[Person] (johnDynamic.data)     == johnStatic)

// ----------------------------------------------------------------------
// schema
assert(  toClsMacro         (johnStatic)           == johnDynamic.schema)
assert(  toClsMacro         [Person]               == johnDynamic.schema)
// from counterpart wouldn't make much sense
```

Assumming the following metadata (schemas):

```scala
case class Person(name: String, age: Int, phones: Seq[String], addresses: Seq[Address])
  case class Address(street: String, city: String, primary: Boolean)    

case class PersonAlt(first: String, last: String, phones: Seq[String], addresses: Seq[Address])

// ---------------------------------------------------------------------------
private val PersonGalliaClass = 
  cls(
      "name"      .string,
      "age"       .int,
      "phones"    .strings,
      "addresses" .clss(
          "street" .string,
          "city"   .string,
          "primary".boolean))
```

and data:

```scala          
private val johnStatic: Person =
  Person(
      name      = "John Smith",
      age       = 32,
      phones    = Seq("123-456-7890", "098-765-4321"),
      addresses = Seq(
        Address("3 Orion Street", "Toronto", primary = true),
        Address("2 Belle Blvd"  , "Lyon",    primary = false)))

// ---------------------------------------------------------------------------
private val johnDynamic: AObj =
  aobj(PersonGalliaClass)(obj(
    "name"      -> "John Smith",
    "age"       -> 32,
    "phones"    -> Seq("123-456-7890", "098-765-4321"),
    "addresses" -> Seq(
        obj("street" -> "3 Orion Street", "city" -> "Toronto", "primary" -> true),
        obj("street" -> "2 Belle Blvd"  , "city" -> "Lyon",    "primary" -> false))) )

```

Of course the point is actually to apply transformations, eg:

```scala
toHeadMacro(johnStatic)
  .toUpperCase('name)
  // ...
  .printCompactJson()    

// preferably to other case classes after a long and convoluted journey
toHeadMacro(johnStatic)
    .fission(_.string("name")).as("first", "last").using(_.splitBy(" ", 2).force.tuple2)
    // ...
  .thn(fromHeadMacro[PersonAlt](_))
  .assert(_ == PersonAlt("John", "Smith", johnStatic.phones, johnStatic.addresses))
```

### Contact
You may contact the author at: <sub><img src="./images/ct.png"></sub>

