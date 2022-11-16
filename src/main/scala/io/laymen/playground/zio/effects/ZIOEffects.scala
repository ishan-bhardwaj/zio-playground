package io.laymen
package io.laymen.playground.zio.effects

import zio._

import scala.io.StdIn

object ZIOEffects {

  /**
   * ZIO[-R, +E, +A] = R is the input type, E is the error type and A is the return value type.
   */

  // Successful ZIO
  val value: ZIO[Any, Nothing, Int] = ZIO.succeed(42)

  // Failed ZIO
  val aFailure: ZIO[Any, String, Nothing] = ZIO.fail("Error occurred")

  /** Suspended ZIO -
   * Error channel is now Throwable the construction of the "suspended" ZIO itself may fail.
   */
  val aSuspendedZIO: ZIO[Any, Throwable, Int] = ZIO.suspend(value)

  // map
  val doubled: ZIO[Any, Nothing, Int] = value.map(_ * 2)

  // flatMap
  val printing: ZIO[Any, Nothing, Unit] = value.flatMap(x => ZIO.succeed(println(x)))

  // for-comprehension
  val program: ZIO[Any, Nothing, Unit] = for {
    _ <- ZIO.succeed(println("Enter your name: "))
    name <- ZIO.succeed(StdIn.readLine())
    _ <- ZIO.succeed(println(s"Welcome: $name"))
  } yield ()

  /**
   * ZIO provides a lot of combinators.
   */
  val zio1: ZIO[Any, Nothing, Int] = ZIO.succeed(42)
  val zio2: ZIO[Any, Nothing, Int] = ZIO.succeed(45)

  // zip - returns the tuple of both values
  val tupledZIO: ZIO[Any, Nothing, (Int, Int)] = zio1.zip(zio2)

  // zipWith - accepts lambda to combine both the values
  val combined: ZIO[Any, Nothing, Int] = zio1.zipWith(zio2)(_ * _)

}
