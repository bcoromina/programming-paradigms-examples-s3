

@main def trampolineJump():Unit=
    // A trampoline is a technique to make recursive programs stack-safe
    // Instead of calling recursively immediately, each step returns a description of the next computation.
    // A loop (run) executes those steps iteratively.


    // Normal recursion consumes stack space and can lead to StackOverflowError for deep recursion.
    def sum(n: Int): Int =
      if n == 0 then 0 else n + sum(n - 1)

    enum Trampoline[A]:

      case Done(value: A)

      case More(thunk: () => Trampoline[A])

      def map[B](f: A => B): Trampoline[B] =
        flatMap(a => Trampoline.Done(f(a)))

      def flatMap[B](f: A => Trampoline[B]): Trampoline[B] =
        this match
          case Done(v) =>
            Trampoline.More(() => f(v))
          case More(thunk) =>
            Trampoline.More(() => thunk().flatMap(f))

      @annotation.tailrec
      final def run: A =
        this match
          case Done(value) =>
            value

          case More(thunk) =>
            thunk().run

    def sumTrampoline(n: Int, acc: Int = 0): Trampoline[Int] =
      if n == 0 then
        Trampoline.Done(acc)
      else
        Trampoline.More(() => sumTrampoline(n - 1, acc + n))

    //A trampoline encodes recursive computation steps as data, usually with a sum type (an ADT).

    // Example
    //println(sum(100000)) // This will throw StackOverflowError
    println(sumTrampoline(100000).run)

    // we jump from the trampoline to the tree
    enum Tree:
      case Leaf(value: Int)
      case Node(left: Tree, right: Tree)

    def sumTree(tree: Tree): Int =
      tree match
        case Tree.Leaf(v) => v
        case Tree.Node(l, r) => sumTree(l) + sumTree(r) // not tail recursive

    def sumT(tree: Tree): Trampoline[Int] =
      tree match

        case Tree.Leaf(v) =>
          Trampoline.Done(v)

        case Tree.Node(l, r) =>
          Trampoline.More(() =>
            sumT(l).flatMap { leftSum =>
              sumT(r).map { rightSum =>
                leftSum + rightSum
              }
            }
          )

    def skewed(n: Int): Tree =
      (0 until n).foldLeft(Tree.Leaf(1)) { (acc, n) =>
        Tree.Node(acc, Tree.Leaf(n))
      }

    println("creating tree..")
    val tree = skewed(10000000)
    println("tree created")

    //println(sumT(tree).run) // The trampoline itself is not stack-safe. See flatMap implementation.

    import scala.util.control.TailCalls.*

    def sumM(tree: Tree): TailRec[Int] =
      tree match
        case Tree.Leaf(v) => done(v)
        case Tree.Node(l, r) =>
          for
            leftSum <- tailcall(sumM(l))
            rightSum <- tailcall(sumM(r))
          yield leftSum + rightSum

    println(sumM(tree).result)