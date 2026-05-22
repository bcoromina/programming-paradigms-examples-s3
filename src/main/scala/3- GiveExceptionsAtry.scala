import scala.util.Try
import scala.util.Success
import scala.util.Failure

@main def GiveExceptionsAtry() =

  // Example of throwing an exception
  def divide(a: Double, b: Double): Double =
    if (b == 0) throw new ArithmeticException("Cannot divide by zero")
    else a / b

  try
    val result = divide(10, 2)
    println(s"Result: $result")
  catch
    case e: ArithmeticException => println(s"Caught an exception: ${e.getMessage}")
  finally
    println("This will always be printed")

  def functionalDivide(a: Double, b: Double): Try[Double] =
    Try(divide(a,b))

  val result = functionalDivide(30, 23)
  result match
    case Success(value) => println(s"Result: $value")
    case Failure(exception) => println(s"Caught an exception: ${exception.getMessage}")

  // You can also use map and flatMap to work with Try
  val incrementedResult: Try[Double] = functionalDivide(30, 23).map(_ + 1)
  
  val result2: Try[Double] = functionalDivide(30, 23).flatMap(x => functionalDivide(x, 23))
  
  // Notice that if the first division fails, the second one will not be executed and the exception will be propagated
  
  // Notice that Try has the same map and flatMap composition operators as Option (Monad and Functor)
  
  // Functor (map: Try[A] => Try[B]). I have a (possible) value inside a box and, if present, I
  // apply a function to it (potentially transforming its type) and put the new value in the same kind 
  // of box. Box is a data structure. If the data structure contains multiple values I apply the function
  // to all of them.
  
  // Monad (flatMap): 
  //    - concatenation of effectful computations. functionalDivide(30, 23).flatMap(x => functionalDivide(x, 23))
  //    - Transform and flatten a nested data structure List(1,2).flatMap(x => List(x, x+1)) => List(1, 2, 2, 3)


  def myRecover(x: Try[Double], r: Throwable => Double): Try[Double] = x match
    case Failure(exception) => Try(r(exception))
    case Success(value) => Success(value)


  // Specific operators
  val default = 0.0
  val p: Try[Double] = incrementedResult.recover((e: Throwable) => default)
  val p2: Try[Double] = incrementedResult.recoverWith(e => Try(22))
  
