// type class pattern
// is used to add new functionality to existing types without modifying their code.
trait JsonSerializable[T]:
  def toJsonT(v: T): String

case class User(name: String, age: Int)
case class House(address: String, price: BigDecimal)

object JsonSerializableInstances:

//  given JsonSerializable[User] with
//    def toJson(p: User): String =
//      s"""{"name": "${p.name}", "age": ${p.age}}"""

    given userSerializer: JsonSerializable[User] =
      new JsonSerializable[User]:
        def toJsonT(p: User): String =
          s"""{"name": "${p.name}", "age": ${p.age}}"""

    given JsonSerializable[House] with
      def toJsonT(p: House): String =
        s"""{"address": "${p.address}", "price": ${p.price}}"""

//    given optionSerializer[T](using s: JsonSerializable[T]): JsonSerializable[Option[T]] with
//      def toJsonT(v: Option[T]): String = v match
//        case Some(value) => s.toJsonT(value)
//        case None        => "{}" // or null. null is a valid json value

    // Composition: If I know how to serialize T, I can serialize Option[T] without knowing the details of T.
    given optionJsonSerializable[T: JsonSerializable]: JsonSerializable[Option[T]] with
      def toJsonT(v: Option[T]): String = v match
        case Some(value) => summon[JsonSerializable[T]].toJsonT(value)
        case None        => "{}" // or null. null is a valid json value



//    extension [T: JsonSerializable](value: T)
//      def toJsonD: String =
//        summon[JsonSerializable[T]].toJson(value)

def toJson[T](value: T)(using js: JsonSerializable[T]): String =
  js.toJsonT(value)

//def toJson[T: JsonSerializable](value: T): String =
//  summon[JsonSerializable[T]].toJsonT(value)

// the required type classes are the properties of the type.
def printList[T: JsonSerializable](l: List[T]): Unit =
  l.foreach { item =>
    println(s"Object: ${item.toJsonExt}")
  }

extension [T: JsonSerializable](value: T)
  def toJsonExt: String =
    summon[JsonSerializable[T]].toJsonT(value)


@main def jsonSerializer(): Unit =
  import JsonSerializableInstances.given

  //ad-hoc polymorphism: We define specific behavior for specific types.
  //vs subtype polymorphism: We define behavior for a type and all its subtypes.
  //vs parametric polymorphism: We define behavior for all types. //def identity[T](x: T): T = x


  //NOTE: function overloading is also ad-hoc polymorphism.
  //Function print does different things for each type String or Int:
  // def print(x: Int)
  // def print(x: String)

  val user = User("Bernat", 44)

  println(user.toJsonExt)

  println(Option(user).toJsonExt)

  println(toJson(user))