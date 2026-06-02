@main def ioMain : Unit =
  case class IO[A]( func: () => A):

    def map[B](f: A => B): IO[B] =
      IO(() => f(func()))

    def flatMap[B](f: A => IO[B]): IO[B] =
      IO(() => f(func()).func())

    def unSafeRun: A = func()


    def readUserFromDb(id: Int): IO[String] =
      IO(() => s"User$id")

    for{
      a <- IO(() => 1)
      b <- readUserFromDb(a)
    }yield a + b