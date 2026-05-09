import UserId.UserId

//An opaque type is:
//
//- represented by an existing runtime type
//- but treated as a distinct type by the compiler outside its scope
object UserId:
  opaque type UserId = Int

  def apply(value: Int): UserId = value

  extension (id: UserId)
    def value: Int = id

// Alternatively, it can be defined as a case class but it requires an extra memory allocation for the wrapper type.
case class UserIdV(value: Int)


@main def opaqueTypesMain(): Unit =
  val userId: UserId = UserId(123)
  println(userId.value) // prints 123

  // The following line would not compile because UserId is opaque and cannot be treated as an Int
  // val intValue: Int = userId