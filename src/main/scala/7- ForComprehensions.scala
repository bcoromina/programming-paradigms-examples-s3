import scala.util.Try

@main def forComprehensions() =

  // For comprehensions are a syntactic sugar for flatMap and map
  def getIntValue(): Option[Int] = Some(1)
  def getStringValor(x: Int): Option[String] = Some(x.toString)
  def getLong(s: String): Option[Long] = Try(s.toLong).toOption

  val result = for {
    intValue <- getIntValue()
    stringValue <- getStringValor(intValue)
    longValue <- getLong(stringValue)
  } yield longValue + 1

  val l = getIntValue()
    .flatMap(x =>
      getStringValor(x)
        .flatMap(y => getLong(y))
        .map(r => r + x + 1)
    )
  // Monads are used to chain effectful computations in a pure functional way. For comprehensions are a syntactic sugar for chaining monadic computations.
  // They allow us to write code that looks imperative but is actually pure functional
  // the chain stops on failure (None in case of Option, Left in case of Either) and the rest of the computations are not executed.
  // This is called short-circuiting behavior.

  // List is also a monad that allows us to chain computations that produce multiple results.
  // The side effect is a computation that produces multiple results.
  val list = List(1, 2, 3)
  val result2 = for {
    x <- list
    y <- List(10, 20, 30)
  } yield x + y

  println(result2) // List(11, 21, 31, 12, 22, 32, 13, 23, 33)
