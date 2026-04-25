@main def SideEffectComposition(): Unit =

  // Composition without side effects
  def addOne(x: Int): Int = x + 1

  def addTwo(x: Int): Int = addOne(addOne(x))

  val addTwoScala: Int => Int = addOne andThen addOne //addOne.andThen(addOne)
  println(addTwoScala(3))

  // Composition with side effects
  def addOneWithSideEffect(x: Int): Int = {
    val result = x + 1
    println("Result: " + result) // println or launch the nuclear missile!
    result
  }
  // the function signature said nothing about the nuclear missile,
  // but it is a side effect that happens when you call the function


  // Can you build a function addTwo (adds two to the incoming Int and prints the result) by
  // composing addOneWithSideEffect ???

  // Naive implementation
  def addTwoWithSideEffects(x: Int): Int =
    val result1 = addOneWithSideEffect(x)
    val result2 = addOneWithSideEffect(result1)
    result2


  // Better implementation using composition
  def functionalAddOneWithSideEffect(x: Int): (Int,String) =
    val result = x + 1
    (result, "Result: " + result)

  def functionalAddTwo(x: Int): (Int, List[String]) =
    val result1 = functionalAddOneWithSideEffect(x)
    val result2 = functionalAddOneWithSideEffect(result1._1)
    (result2._1, List(result1._2, result2._2))

  def functionalAddTwoWithSideEffects(x: Int): Int =
     val result = functionalAddTwo(x)
     println(result._2.last)
     result._1

  functionalAddTwoWithSideEffects(5)
