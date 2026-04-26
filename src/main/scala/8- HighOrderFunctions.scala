@main def HighOrderFunctions() = 
  // A higher-order function is a function that takes another function as a parameter or returns a function as a result
  def applyFunctionTwice(f: Int => Int, x: Int): Int = f(f(x))

  def addOne(x: Int): Int = x + 1

  println(applyFunctionTwice(addOne, 5)) // This will print 7

  // You can also use anonymous functions (lambdas) with higher-order functions
  println(applyFunctionTwice(x => x * 2, 3)) // This will print 12
  
  enum MyEnum:
    case A, B, C
    
  def selectFunction(e: MyEnum): Int => Int = e match
    case MyEnum.A => 
        x => x + 1
    case MyEnum.B => 
        x => x * 2
    case MyEnum.C => x => x - 1 
