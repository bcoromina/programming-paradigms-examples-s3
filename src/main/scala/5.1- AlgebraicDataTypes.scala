@main def AlgebraicDataTypes(): Unit =

  // Algebraic Data Types are a way to model data in a type-safe way.
  // They are called algebraic because they can be combined using algebraic operations like product and sum.

  // The number of possible values of a type can be calculated using the following rules:
  // - For a product type (case class), the number of possible values is the product of the number of possible values of its fields.
  //   For example, if a case class has two fields, one of type A with n possible values and another of type B with m possible values,
  //   then the total number of possible values for the case class is n * m.
  // - For a sum type (sealed trait with case objects or case classes), the number of possible values
  //   is the sum of the number of possible values of its subtypes.

  // Product types (case classes)
  // Cartesian product in set theory.
  case class User(id: Int, name: String, email: String)

  // Sum types (sealed traits and case objects)
  // Disjoint union in set theory.
  sealed trait Shape
  case class Circle(radius: Double) extends Shape
  case class Rectangle(width: Double, height: Double) extends Shape
  case object Point extends Shape

  // Is the match exhaustive? (uncomment sealed in the Shape definition to see the warning disappear)
  def stringify(shape: Shape): String =
    shape match
      case Circle(r) => s"Circle with radius $r"
      case Rectangle(w, h) => s"Rectangle with width $w and height $h"
      //case Point => "Point"

  // You can also have recursive data structures
  sealed trait Tree
  case class Node(value: Int, left: Tree, right: Tree) extends Tree
  case object Empty extends Tree

  //Sum types with enums (new in scala 3)
  enum TreeV:
    case Node(value: Int, left: TreeV, right: TreeV)
    case Empty

  enum TreeG[+A]:
    case Node(value: A, left: TreeG[A], right: TreeG[A])
    case Empty

  // Union types new in scala 3
  // No extra allocation for the wrapper type like Either
  type IntOrString = Int | String // this is a type alias for the union type
  val a: IntOrString = 23
  val b: IntOrString = "hello"
  val c: Int | String = 24


  val x = if (true) 1 else "a" // Annotate the type. Sadly is Any ! (༎ຶ⌑༎ຶ) but this is IntelliJ's fault
  // x: Int | String
  println("Handle " + handle(x)) // Compiler's type inference is working fine

  def handle(input: Int | String): String =
    input match
      case i: Int => s"number: ${i + 1}"
      case s: String => s"text: ${s.toUpperCase}"

  def handle2(input: IntOrString): String = ???

  // Wait I can use it to create expressive types!
  type ErrorOr[A] = A | Throwable
  def divide(a: Double, b: Double): ErrorOr[Double] =
    if b == 0 then new ArithmeticException("div by zero") else a / b

  // but I want ErrorOr to be a Monad, so I need to add map and flatMap

  // Extension functions
  case class User2(name: String, age: Int)

  extension (u: User2)
    def isAdult: Boolean = u.age >= 18

  val user = User2("Alice", 20)
  user.isAdult


  extension [A](e: ErrorOr[A])
    def map[B](f: A => B): ErrorOr[B] =
      e match
        case t: Throwable => t
        case a:A  => f(a) // Not safety because of type erasure at runtime! if is not a Throwable is an A

    def flatMap[B](f: A => ErrorOr[B]): ErrorOr[B] =
      e match
        case t: Throwable => t
        case a: A => f(a) // Not safety because of type erasure at runtime!

  // Generics info is erased at runtime, so we cannot distinguish type A at runtime.
  // because of run type erasure we are converting A | Throwable to Object | Throwable
  // so A | Throwable is not equivalent to Either[Throwable, A]
  // Either[Throwable, A] it's a more type safe representation.


  val p = divide(10, 2).map(_ + 1)
  println("Result map: " + p)

  val p2 = divide(10, 2).flatMap(x => divide(x, 2))
  println("Result flatMap: " + p2)

  // Now I know how to build Monads? yes but... algebraic laws are missing!!!

  // Intersection types are a powerful feature in Scala 3 that allows us to combine multiple types into one.
  // Intersection like in set theory, the intersection of two sets A and B is the set of elements that are in both A and B.

  trait HasName:
    def name: String

  trait HasAge:
    def age: Int


  def describe(x: HasName & HasAge): String =
    s"${x.name} is ${x.age}"

  case class User3(name: String, age: Int) extends HasName, HasAge

  describe(User3("Bob",33))

  def describeName(x: HasName): String = s"Name: ${x.name}"



