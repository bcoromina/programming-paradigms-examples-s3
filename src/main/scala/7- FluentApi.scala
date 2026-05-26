@main def FluentApi() = {

  // about immutable lists
  List(1,2,3).last
  List(1,2,3).head
  
  val oneList = 23 :: List(1,2,3)

  val myList = 23 :: 1 :: 2 :: 3 :: Nil

  List(1,2,3) match {
    case first :: second :: tail =>
      println(s"First: $first, Second: $second, Tail: $tail")
    case head :: tail =>
      println(s"Head: $head, Tail: $tail")
    case Nil => println("Empty list")
  }

  List(1,2,3,4,5).filter(_ % 2 == 0).map(_ * 2).foreach(println)

  List(1,2).sum
  List(1,2,3,4,5,6,7,8).partition(_ % 2 == 0) // (List(2,4,6,8), List(1,3,5,7))

  val grouped: Map[String, List[(String, Int)]] = List( ("a",1), ("b", 3), ("a", 5), ("b",1) ).groupBy((a, b) => a)

  // if I call mapValues on the map I get a lazy version of the Map.
  // the function is not applied until I access the value of the map.
  // This is useful if I want to apply a function to the values of the map without creating a new map.
  val r2: Map[String, Int] = grouped.view.mapValues(l => l.map(_._2).sum).toMap
  val r3 = grouped.map{ case (k, v) => (k, v.map(_._2).sum)} // this is not lazy
  
  // let's implement a sum with foldLeft
  List(1,2,3).foldLeft(0){ case (acc, v) => acc + v }

  //let's implement a GENERIC sum with foldLeft

  //def sum[T](list: List[T], zero: T) : T = list.foldLeft(zero){ case (acc, v) => acc + v }

  trait Monoid[T] {
    def zero: T
    def combine(a: T, b: T): T
  }

  def sumM[T: Monoid](list: List[T]): T =
    val monoid = implicitly[Monoid[T]]
    list.foldLeft(monoid.zero)(monoid.combine)

  def sumM2[T](list: List[T])(using monoidInstance: Monoid[T]): T =
    list.foldLeft(monoidInstance.zero)(monoidInstance.combine)


  // Monoid Instances for user and int
  given Monoid[Int] with
    def zero: Int = 0
    def combine(a: Int, b: Int): Int = a + b

  class User(val name: String, val age: Int)
  given Monoid[User] with
    def zero: User = new User("", 0)
    def combine(a: User, b: User): User = new User(a.name + b.name, a.age + b.age)

  // groupBy key using fold left (and sum the values)
  List( ("a",1), ("b", 3), ("a", 5), ("b",1) ).foldLeft(Map.empty[String, Int]){ case (acc, (k, v)) =>
    val currentValue = acc.getOrElse(k, 0)
    acc.updated(k, currentValue + v) //is not update, updated returns a new map.
  }
  
  // generic group by key for List[(K,V)] 
//  def wonderfulGroupByKey[K,V](list: List[(K, V)]): Map[K, List[V]] =
//    list.foldLeft(Map.empty[K, List[V]]){ case (acc, (k, v)) =>
//      val currentValue = acc.getOrElse(k, Nil)
//      acc.updated(k, currentValue :+ v) 
//    }

  extension [K,V](listOfPairs: List[(K,V)])
    def wonderfulGroupByKey: Map[K, List[V]] =
      listOfPairs.foldLeft(Map.empty[K, List[V]]){ case (acc, (k, v)) =>
        val currentValue = acc.getOrElse(k, Nil)
        acc.updated(k, v :: currentValue) // should revert the list?
      }
  val groupedByKey = List( ("a",1), ("b", 3), ("a", 5), ("b",1) ).wonderfulGroupByKey
  //List(1,2,3,4).wonderfulGroupByKey // This will not compile because the type of the list is not List[(K,V)]
  
  
  
  // mapValues with fold left
  Map("a" -> List(1, 5), "b" -> List(3, 1)).foldLeft(Map.empty[String,Int]){
    case (acc, (k, v)) => acc.updated(k, v.sum)
  }

  // Sets
  val a = Set(1,2,3,4)
  val b = Set(3,4,5,6)
  a.union(b) // Set(1,2,3,4,5,6)
  a.intersect(b) // Set(3,4)
  a.diff(b) // Set(1,2)
  b.diff(a) // Set(5,6)


  // Lazy list

  //[Declarative] define
  val naturals = LazyList.iterate(0)( _ + 1) // This is an infinite stream of natural numbers

  //[Declarative] transform
  val transformedNaturals: Map[String, LazyList[Int]] = naturals
    .map(_ + 2)
    .groupBy(e => if (e % 2 == 0) "even" else "odd")
   
  //[Imperative] materialize. Execute the computation.
   val materialized: Map[String, List[Int]] = 
     transformedNaturals.view.mapValues(_.take(5).toList).toMap



}


