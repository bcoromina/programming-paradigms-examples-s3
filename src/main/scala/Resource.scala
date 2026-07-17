//hello

trait Monad[F[_]] {
  def pure[A](a: A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def map[A, B](fa: F[A])(f: A => B): F[B] =
    flatMap(fa)(a => pure(f(a)))
}

object Monad:
  extension [F[_], A](fa: F[A])
    def flatMap[B](f: A => F[B])(using M: Monad[F]): F[B] = M.flatMap(fa)(f)
    def map[B](f: A => B)(using M: Monad[F]): F[B] = M.map(fa)(f)

case class IO[A](func: () => A):

  def map[B](f: A => B): IO[B] =
    IO(() => f(func()))

  def flatMap[B](f: A => IO[B]): IO[B] =
    IO(() => f(func()).func())

  def unsafeRun: A =
    func()

object MonadInstances:
  given Monad[IO] with
    def pure[A](a: A): IO[A] = IO(() => a)
    def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] = fa.flatMap(f)

final case class Resource[F[_], A](
                                    acquire: F[A],
                                    release: A => F[Unit]
                                  ) {

  def use[B](f: A => F[B])(using M: Monad[F]): F[B] =
    M.flatMap(acquire) { a =>
      M.flatMap(f(a)) { b =>
        M.flatMap(release(a)) { _ =>
          M.pure(b)
        }
      }
    }
//    for {
//      a <- acquire
//      b <- f(a)
//      _ <- release(a)
//    } yield b
}


import java.io.BufferedReader
import java.io.FileReader
import java.nio.file._


@main def Resource2(): Unit =
  import MonadInstances.given

  val dir = System.getProperty("user.dir");
  System.out.println(dir);
  val path = "/src/main/scala/Resource.scala"
  val fileResource: Resource[IO, BufferedReader] =
    Resource[IO, BufferedReader](
      acquire = IO(() => new BufferedReader(new FileReader(path))),
      release = reader => IO(() => reader.close())
    )

//  val e = fileResource.use { reader =>
//    IO{() => reader.readLine()}
//  }
//
//  e.unsafeRun