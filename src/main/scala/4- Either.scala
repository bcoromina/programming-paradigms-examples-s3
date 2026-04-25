@main def Either(): Unit =

  // Use either to carry errors as values (instead of throwing exceptions)
  def getIntValue(x: Int): Either[String,Int] = {
    if(x > 3){
      Right(23)
    }else{
      Left("fatal error: x less than 3")
    }
  }

  // Either is Right biased, so map and flatMap will operate on the Right value if it exists
  val rr: Either[String, Int] = getIntValue(24).map(_ + 1)

  getIntValue(24) match {
    case Left(value) => println(s"Error: $value")
    case Right(value) => println(s"Success: $value")
  }

  // If I tell you that Either is a Monad you already know that it has a map a flatMap and how
  // they work. Isn't it amazing? You can just say "Either is a Monad" and you already know how to use it.

  // Either has map so is a Functor but not a bifunctor. To be a bifunctor it should have a bimap
//  def bimap[A, B, C, D](e: Either[A, B])(f: A => C, g: B => D): Either[C, D] =
//    e match {
//      case Left(a) => Left(f(a))
//      case Right(b) => Right(g(b))
//    }

  extension [A, B](e: Either[A, B])
    def bimap[C, D](f: A => C, g: B => D): Either[C, D] =
      e match
        case Left(a)  => Left(f(a))
        case Right(b) => Right(g(b))
  
  val p: Either[String, Int] = Right(23)
 
  p.bimap(str => s"Error: $str", i => i + 1)
  
  // I've just added a new function to Either without modifying its code. 
  // This is the power of extension methods and type classes. 
  // I can add new functionality to existing types without modifying their code.
  // This is a very powerful technique that allows us to write code that is more modular and reusable.
  // This is one of the key features of functional programming, and it is one of the reasons why we love it so much.
  // I feel like a child in a candy store and I can take whatever I want, nobody is watching me.