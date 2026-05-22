@main def OptionFirstContact(): Unit =

  // Option is a container that can either contain a value (Some) or be empty (None)
  val someValue: Option[Int] = Some(42)
  val noValue: Option[Int] = None
  // Notice Option is a union type

  someValue.get // Don't do this! It will throw an exception if the Option is None
                // NPE vs NoSuchElementException
  noValue.get // This will throw an exception for sure

  val default = 23
  val myValue = someValue.getOrElse(default)

  // You can use pattern matching to extract the value from an Option
  def describeOption(opt: Option[Int]): String =
    opt match
      case Some(value) => s"The value is $value"
      case None => "No value"

  // You can also use higher-order functions like map and flatMap to work with Options
  val incrementedValue: Option[Int] = someValue.map(x => x + 1) // Some(43)
  val incrementedNoValue: Option[Int] = noValue.map(_ + 1) // None The function is not applied

  println(incrementedValue) // Some(43)
  println(incrementedNoValue) // None

  Option{
    // This block will be executed and the result will be wrapped in Some
    val x = 10
    x + 1
  }
  
  val someOtherValue: Option[Int] = Some(10)

  // If both optionals are Some we want to add their values,
  // if one of them is None we want to return None
  val p: Option[Option[Int]] = incrementedValue.map(x =>
    someOtherValue match 
      case Some(v) => Some(x + v)
      case None => None
  )

  val p2: Option[Int] = incrementedValue.flatMap(x =>
    someOtherValue match {
      case Some(v) => Some(x + v)
      case None => None
    }
  )