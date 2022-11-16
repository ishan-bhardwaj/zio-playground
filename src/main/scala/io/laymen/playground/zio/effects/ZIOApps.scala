package io.laymen
package io.laymen.playground.zio.effects

import zio._

object ZIOApps {

  /**
   * Running ZIO Applications.
   */

  // Method 1 - Manual way
  object ManualApp {

    def main(args: Array[String]): Unit = {

      // Runtime involves thread pool and mechanisms by which ZIOs can be evaluated.
      val runtime = Runtime.default

      // Allows us to debug the code regardless of whether the ZIO runs on main thread or some other.
      implicit val trace: Trace = Trace.empty

      val zio = ZIO.succeed(45)

      Unsafe.unsafeCompat { implicit unsafe =>
        runtime.unsafe.run(zio)
      }
    }

  }

  // Method 2 - Mixing with built-in ZIOAppDefault - provides runtime, trace etc.
  object BetterApp extends ZIOAppDefault {
    override def run = ZIO.succeed(45).debug  // the debug method automatically prints the result
  }

  /**
   * Note that there is also a super-generic ZIOApp trait which we can also override
   * but it requires a lot more configuration and that's why ever harder to configure.
   * So, we almost never use it.
   */

}
