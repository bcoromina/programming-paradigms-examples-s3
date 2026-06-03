@main def futureMain(): Unit =
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  val future1 = Future {
    Thread.sleep(1000)
    println("Future 1 completed")
    42
  }

  val future2 = Future {
    Thread.sleep(500)
    println("Future 2 completed")
    "Hello, Future!"
  }

  //future1.foreach(result => println(s"Result of Future 1: $result"))
  //future2.foreach(result => println(s"Result of Future 2: $result"))

  for{
    result1 <- future1
    result2 <- future2
  } yield {
    println(s"Combined results: $result1 and $result2")
  }

  // Keep the main thread alive to see the results
  Thread.sleep(2000)