import Recursion.{impFibonacci, lazyFacto, lazyFactorial, lazyFibonacci}

import scala.annotation.tailrec

@main def RecursionMain() =
  println("Lazy factorials: " + lazyFactorial.take( 5 ).toList)
  assert(impFibonacci(10) == lazyFibonacci().take(10).toList)


object Recursion:

  // 5! = 5 * 4 * 3 * 2 * 1
  def impFactorial(n: Int): Int = {
    var acc = 1
    var count = n
    // n * (n-1) * (n-2) * ... * 1
    while(count > 1){
      acc = acc * count
      count = count - 1
    }
    acc
  }

  // 0, 1, 1, 2, 3, 5, 8, 13, 21, ...
  def impFibonacci(n: Int): List[Int] = {
    val arr: Array[Int] = new Array(n)
    for(i <- 0 until n){
      if(i == 0){
        arr(0) = 0
      }
      else if(i == 1){
        arr(1) = 1
      }
      else{
        arr(i) = arr(i-1) + arr(i-2)
      }
    }
    arr.toList
  }

  // 0, 1, 1, 2, 3, 5, 8, 13, 21, ...
  def recFibonacci(n: Int): List[Int] = {
    @tailrec
    def fibo(i: Int, a: Int, b: Int, acc: List[Int]): List[Int] = {
      if (i >= n) acc.reverse
      else fibo(i + 1, b, a + b, a :: acc)
    }

    fibo(0, 0, 1, Nil)
  }

  def recFactorial(n: Int) = {
    @tailrec
    def facto(count: Int, acc: Int): Int ={
      if(count == 1) acc
      else
        facto(count-1, acc * count)
    }
    facto(n,1)
  }

  def lazyFibonacci(): LazyList[Int] = {
    def fibo(a: Int, b: Int): LazyList[Int] = a #:: fibo(b, a + b)
    fibo(0,1)
  }

  val lazyFactorial: LazyList[BigInt] = {
    def loop(n: Int, acc: BigInt): LazyList[BigInt] =
      acc #:: loop(n + 1, acc * (n + 1))
    loop(0, 1)
  }
  //(0,1) 1 :: loop(1,1)
  //      1 :: 1 :: loop(2,2)
  //      1 :: 1 :: 2 :: loop(3,6)
  //      1 :: 1 :: 2 :: 6 :: loop(4,24)
  //      List(1, 1, 2, 6, 24, loop(5,120))

  def lazyFacto(n: Int): BigInt = lazyFactorial.take(n + 1 ).last


  val l = impFactorial(4)
  val f = recFactorial(4)
  val h = lazyFacto(4)

  assert(impFibonacci(10) == recFibonacci(10))
  assert(impFibonacci(10) == lazyFibonacci().take(10).toList)


