@main def functions(): Unit =
  // funtions as values
  def addOne(x: Int): Int = x + 1
  val v: Int => Int = addOne

  // functions as parameters
  def applyFunctionTwice(f: Int => Int, x: Int): Int = f(f(x))
  println(applyFunctionTwice(addOne, 5)) // This will print 7

  // functions as return values
  def selectFunction(e: String): Int => Int = e match
    case "addOne" => x => x + 1
    case "addTwo" => x => x + 2
    case "multiplyByTwo" => x => x * 2
    case _ => x => x // identity function

  val selectedFunction = selectFunction("addTwo")
  println(selectedFunction(5)) // This will print 7

  // functions can also be anonymous (lambdas)
  val addThree: Int => Int = x => x + 3

  // functions can also be partially applied
  def add(x: Int, y: Int): Int = x + y
  val addFive: Int => Int = add(5, _)
  println(addFive(10)) // This will print 15

  // functions can also be curried
  def curriedManuallyAdd(x: Int)(y: Int): Int = x + y
  val addSix: Int => Int = curriedManuallyAdd(6)
  println(addSix(10)) // This will print 16

  val addCurried: Int => Int => Int = add.curried

  val addTwentyThree = addCurried(23)
  println(addTwentyThree(10)) // This will print 33

  // functions can also be partial
  def divide: PartialFunction[(Int,Int), Double] =
    case (a, b) if b != 0 => a / b

  val result = divide(10, 2) // This will return 5.0

  divide.isDefinedAt(10,2) // This will return true
  divide.isDefinedAt(10,0) // This will return false

  // You can also use the lift method to convert a partial function into a total function that returns an Option
  val liftedDivide: ((Int, Int)) => Option[Double] = divide.lift

