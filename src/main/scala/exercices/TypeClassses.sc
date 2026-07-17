case class Product(name: String, price: Double)

case class User(
                 name: String,
                 age: Int,
                 purchases: List[Product]
               )

case class Book(title: String, year: Int)


// -------------------------------------
// Type class
// -------------------------------------

trait CsvEncoder[A]:
  def encode(value: A): List[String]


// -------------------------------------
// Type class instances
// -------------------------------------

object CsvEncoder:



  given CsvEncoder[Product] with
    def encode(product: Product): List[String] =
      List(
        product.name,
        product.price.toString
      )

  given CsvEncoder[Book] with
    def encode(book: Book): List[String] =
      List(
        book.title,
        book.year.toString
      )


  given CsvEncoder[User](using productEncoder: CsvEncoder[Product]) with
    def encode(user: User): List[String] =
      List(user.name, user.age.toString) ++ user.purchases.flatMap(productEncoder.encode)


//  given CsvEncoder[User] with
//    def encode(user: User): List[String] =
//      val productEncoder = summon[CsvEncoder[Product]]
//
//      List(user.name, user.age.toString) ++
//        user.purchases.flatMap(productEncoder.encode)


// -------------------------------------
// Generic CSV writer
// -------------------------------------

def writeCsv[A](values: List[A])(using
                                 enc: CsvEncoder[A]
): String =

  values
    .map(value => enc.encode(value).mkString(","))
    .mkString("\n")


// -------------------------------------
// Extension syntax
// -------------------------------------

extension [A](value: A)

  def toCsv(using enc: CsvEncoder[A]): String =
    enc.encode(value).mkString(",")


// -------------------------------------
// Demo
// -------------------------------------

@main def runCsvExample(): Unit =

  import CsvEncoder.given

  val products =
    List(
      Product("Keyboard", 99.99),
      Product("Mouse", 49.50)
    )

  val users =
    List(
      User(
        "Alice",
        25,
        List(
          Product("Keyboard", 99.99),
          Product("Mouse", 49.50)
        )
      ),
      User(
        "Bob",
        31,
        List(
          Product("Monitor", 250.0)
        )
      )
    )

  val books =
    List(
      Book("Functional Programming", 2024),
      Book("Scala for Beginners", 2023)
    )

  println("=== Products ===")
  println(writeCsv(products))

  println()

  println("=== Users ===")
  println(writeCsv(users))

  println()

  println("=== Books ===")
  println(writeCsv(books))

  println()

  println("=== Single value ===")
  println(users.head.toCsv)


  //Pregunta 1
  // extensibilidad sin modificar codigo existente
  // Composicion del comportamiento de los tipos
  // (User tiene una lista de Product, y el encoder de User usa el encoder de Product)
  // Principio Open/Closed


  // Pregunta 2
  trait CsvEncoder[A]:
    def encode(value: A, style: CsvStyle): List[String]
  enum CsvStyle:
    case Pretty, Compact
  // Pregunta 3 AdHoc -> para este tipo en concreto, no hay una implementación general para todos los tipos.
  // El comportamiento se define específicamente para cada tipo caso por caso.
  // No hay una regla general compartida entre los tipos.
