abstract class MyList[+T] {
  // The >>: method is a right-associative operator for adding an element to the front of the list.
  // In Scala, an operator is right-associative if its name ends with :
  // The operatior is right associative it indicates the compiler how to parse expressions that use the operator. 
  // For example, the expression 1 >>: 2 >>: 3 >>: MyEmpty is parsed as 1 >>: (2 >>: (3 >>: MyEmpty)).
  
  // B is a supertype of T, which allows us to add an element of type B to a list of type T.
  // This is necessary because MyList is covariant in T, so we can only add elements that are supertypes of T.
  // We have a list of apples, we add a fruit to it, we get a list of fruits.
  def >>:[B >: T](elem: B): MyList[B] = MyCons(elem, this)

  //def append(elem: T): MyList[T] = MyCons(elem, this) // Covariant type occurs in a contravariant position,

}
// trait MyList[+T] // covariant in T. Remove the + and see what happens when I use MyEmpty
case object EmptyList extends MyList[Nothing]
// Nothing is the bottom type (a subtype of all types), so MyEmpty can be used as MyList[Int], MyList[String], etc.
// If MyList were invariant in T, then MyEmpty would not be a valid MyList[Int] or MyList[String], etc.
// because it would be MyList[Nothing] and not MyList[Int] or MyList[String].
case class MyCons[A](elem: A, tail: MyList[A]) extends MyList[A]

class Fruit
class Apple extends Fruit

@main def myEmpty():Unit =
  val fruit: Fruit = new Apple

  val myList: MyList[Int] = EmptyList

  println(myList)

  val integerList: MyList[Int] = 1 >>: 2 >>: 3 >>: EmptyList

  // How to add an element to a covariant list? widen the type parameter.
  val apples: MyList[Apple] = ???
  val fruits: MyList[Fruit] = apples // allowed by covariance

  val result = fruits.>>:(new Fruit {}) // adding a non-Apple so I get a MyList[Fruit]
  // annotate the type of result to see that it is MyList[Fruit]
  
  // Contravariance example

  def useFruit(fruit: Fruit) = ???
  useFruit(new Apple) // This is fine because Apple is a subtype of Fruit

  class Juicer[-A <: Fruit] {
    def makeJuice(fruit: A): String = s"Juicing a ${fruit.getClass.getSimpleName}"
  }

  def useJuicer(juicer: Juicer[Apple], apple: Apple): Unit = juicer.makeJuice(apple)

  useJuicer(new Juicer[Apple], new Apple)
  useJuicer(new Juicer[Fruit], new Apple) // This is fine because Juicer[Fruit] is a subtype of Juicer[Apple]

  // In functions the parameter type is contravariant and the return type is covariant.
  type MyFunction[-A, +B] = A => B
