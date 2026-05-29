trait Animal
case class Dog(name: String) extends Animal
case class Cat(name: String) extends Animal


@main def main(): Unit = {

  def feedAnimal(animal: Animal): Unit = animal match
    case Dog(name) => println(s"Feeding dog: $name")
    case Cat(name) => println(s"Feeding cat: $name")


  feedAnimal(Dog("Buddy")) // Liskov Substitution Principle (LSP) is satisfied because we can use a Dog where an Animal is expected.
  // this happens because Dog is a subtype of Animal, so we can use a Dog wherever an Animal is expected. This is the essence of the Liskov Substitution Principle (LSP).

  def foo(f: Dog => Animal) : Animal = {
    val dog = Dog("Rex")
    val animal: Animal = f(dog)
    animal
  }

  //COVARIANCE EXAMPLE

  // Return type covariance example
  val funcA: Dog => Dog = _ => Dog("Rex")
  foo(funcA) // When applied in foo, f(dog) will return a Dog, which is a subtype of Animal, so this is valid and satisfies LSP.
  // funcA  is a subtype of Dog => Animal. (Dog => Dog) <: (Dog => Animal)

  // The return type of a function is a covariant potion.
  // MyList[+T] is covariant in T, so we can use MyList[Dog] where MyList[Animal] is expected. This is because MyList[Dog] is a subtype of MyList[Animal].

  def bef(l: List[Animal]): Unit = ???

  val dogs: List[Dog] = List(Dog("Buddy"), Dog("Rex"))
  bef(dogs) // This is fine because List[Dog] is a subtype of List[Animal] due to covariance. This is another example of LSP in action.
  // Command+Click al List to see that List is covariant in its type parameter.

  // Let's represent functions as
  trait MyFunction1[-A, +B]:
    def apply(a: A): B

  object addOneToString extends MyFunction1[Int, String]:
    def apply(a: Int): String =
      (a + 1).toString


  val a: String = addOneToString(2)


  // CONTRAVARIANCE EXAMPLE

  val funcB: Animal => Animal = _ => Dog("Rex")

  foo(funcB)// This means (Animal => Animal) <= (Dog => Animal) because the parameter type is contravariant. This is also valid and satisfies LSP.
  // This is semantically correct because in foo I need a function that transforms Dogs (into Animals) and
  // I'm providing function B that transforms any Animal (including Dogs). So this is valid and satisfies LSP.

  // this is counterintuitive becase Dog <: Animal, but (Animal => Animal) <: (Dog => Animal)


  class MyFeeder[-A <: Animal] {
    def feed(animal: A): Unit = println(s"Feeding a ${animal.getClass.getSimpleName}")
  }
  
  def useFeeder(feeder: MyFeeder[Dog], dog: Dog): Unit = feeder.feed(dog)
  
  val feeder: MyFeeder[Animal] = new MyFeeder[Animal]
  useFeeder(feeder, Dog("Buddy")) // This means MyFeeder[Animal] is a subtype of MyFeeder[Dog]. 
  // So MyFeeder and its type parameter have a covariant relation.
  
  
  
}