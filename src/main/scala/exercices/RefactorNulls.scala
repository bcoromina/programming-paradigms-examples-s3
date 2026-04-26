package exercices

//Precondición: Option, Union types, flatMap, map & for comprehensions

object RefactorNulls:
  case class User(id: Int, name: String, email: String)
  
  private val users = Map(
    1 -> User(1, "Alice", "alice@example.com"),
    2 -> User(2, "Bob", null),  // Missing email
    3 -> User(3, "Charlie", "charlie@example.com")
  )

  def findUserById(id: Int): User =
    if (users.contains(id)) users(id) else null

  def getEmail(user: User): String =
    if (user != null && user.email != null) user.email else null

  def printUserEmail(userId: Int): String = {
    val user = findUserById(userId)
    if (user != null) {
      val email = getEmail(user)
      if (email != null) {
        //println(s"User email: $email")
        email
      } else {
        //println("Email not available")
        null
      }
    } else {
      //println("User not found")
      null
    }
  }
