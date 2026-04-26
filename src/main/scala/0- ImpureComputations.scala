import java.io.{File, PrintWriter}
import scala.util.Random

object ImpureComputations {

  def printMessage(msg: String): Unit = {
    println(s"Message: $msg")
  }
  ////////////////////////////////////////////////////
  var counter = 0

  def incrementCounter(): Unit = {
    counter += 1
  }
  ////////////////////////////////////////////////////
  val listBuffer = scala.collection.mutable.ListBuffer(1, 2, 3)

  def addElement(elem: Int): Unit = {
    listBuffer += elem
  }

  ///////////////////////////////////////////////////

  def fillList(num: Int): List[Int] = {
    var c = 0
    val list = scala.collection.mutable.ListBuffer[Int]()
    while(c < num){
      list.addOne(c)
      c = c + 1
    }
    list.toList
  }

  ////////////////////////////////////////////////////
  def readUserInput(): String = {
    scala.io.StdIn.readLine() // Side effect: depends on external input
  }
  ////////////////////////////////////////////////////
  def divide(a: Int, b: Int): Int = {
    if (b == 0) throw new ArithmeticException("Division by zero!")
    else a / b
  }

  def foo(): Nothing = {
    throw new Exception("Something went wrong!");
  }

  def unCallable(a: Nothing) = 23

  ////////////////////////////////////////////////////
  def writeFile(filename: String, content: String): Unit = {
    val writer = new PrintWriter(new File(filename))
    writer.write(content)
    writer.close()
  }
  ////////////////////////////////////////////////////
  def getCurrentTime(): Long = {
    System.currentTimeMillis()
  }

  ///////////////////////////////////////////////////
  def getRandomNumber(): Int = {
    Random.nextInt(100)
  }

  ///////////////////////////////////////////////////

  import scala.io.Source

  def fetchWebPage(url: String): String = Source.fromURL(url).mkString


  ///////////////////////////////////////////////////
  import java.sql.{Connection, DriverManager}

  def insertIntoDatabase(): Unit = {
    val connection: Connection = DriverManager.getConnection("jdbc:h2:mem:test")
    val statement = connection.createStatement()
    statement.executeUpdate("CREATE TABLE users (id INT, name VARCHAR(100))")
    statement.executeUpdate("INSERT INTO users VALUES (1, 'Alice')") // Side effect: modifies database
    connection.close()
  }
  ///////////////////////////////////////////////////

}

object MyTests extends App {
  ImpureComputations.printMessage("hola que tal")
}
