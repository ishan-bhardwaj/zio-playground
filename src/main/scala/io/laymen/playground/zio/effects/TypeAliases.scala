package io.laymen
package io.laymen.playground.zio.effects

import zio._

object TypeAliases {

  /**
   * Type aliases of ZIOs -
   */

  // UIO[A] = ZIO[Any, Nothing, A] => No requirements, cannot fail, produces A
  val aUIO: UIO[Int] = ZIO.succeed(45)

  // URIO[R, A] = ZIO[R, Nothing, A] => R requirement, cannot fail, produces A
  val aURIO: URIO[Int, Int] = ZIO.succeed(45)

  // Task[A] = ZIO[Any, Throwable, A] => No requirements, can fail with a Throwable, produces A
  val aSuccessfulTask: Task[Int] = ZIO.succeed(45)
  val aFailedTask: Task[Int] = ZIO.fail(new RuntimeException("Error"))

  // RIO[R, A] = ZIO[R, Throwable, A] => R requirement, can fail with Throwable, produces A
  val aSuccessfulRIO: RIO[Int, Int] = ZIO.succeed(45)
  val aFailedRIO: RIO[Int, Int] = ZIO.fail(new RuntimeException("Error"))

  // IO[E, A] = ZIO[Any, E, A] => No requirements, can fail with E, produces A
  val aSuccessfulIO: IO[String, Int] = ZIO.succeed(45)
  val aFailedIO: IO[String, Int] = ZIO.fail("Error")

}
