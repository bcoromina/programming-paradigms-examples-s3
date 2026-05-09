// type class pattern
// is used to add new functionality to existing types without modifying their code.
trait JsonSerializable[T]:
  def toJson(v: T): String

case class User(name: String, age: Int)
case class House(address: String, price: BigDecimal)

object JsonSerializableInstances

  given JsonSerializable[User] with
    def toJson(p: User): String =
      s"""{"name": "${p.name}", "age": ${p.age}}"""

//  given userSerializer: JsonSerializable[User] =
//    new JsonSerializable[User]:
//      def toJson(p: User): String =
//        s"""{"name": "${p.name}", "age": ${p.age}}"""

  given JsonSerializable[House] with
    def toJson(p: House): String =
      s"""{"address": "${p.address}", "price": ${p.price}}"""

  given optionSerializer[T](using s: JsonSerializable[T]): JsonSerializable[Option[T]] with
    def toJson(v: Option[T]): String = v match
      case Some(value) => s.toJson(value)
      case None        => "{}" // or null. null is a valid json value

//  given [T: JsonSerializable]: JsonSerializable[Option[T]] with
//    def toJson(v: Option[T]): String = v match
//      case Some(value) => value.toJson
//      case None => "{}"

def toJsonT[T: JsonSerializable](value: T): String =
  summon[JsonSerializable[T]].toJson(value)

extension [T: JsonSerializable](value: T)
  def toJson: String =
    summon[JsonSerializable[T]].toJson(value)


@main def jsonSerializer(): Unit =
  import JsonSerializableInstances._
  val user = User("Bernat", 44)
  println(user.toJson)

  println(Option(user).toJson)

  println(toJsonT(user))