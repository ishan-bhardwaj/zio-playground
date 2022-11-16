package io.laymen
package io.laymen.playground.zio.effects

import zio._

object RunningZIO {

  def main(args: Array[String]): Unit = {

    // Runtime involves thread pool and mechanisms by which ZIOs can be evaluated.
    val runtime = Runtime.default

    // Allows us to debug the code regardless of whether the ZIO runs on main thread or some other.
    implicit val trace: Trace = Trace.empty

    val zio = ZIO.succeed(45)

    Unsafe.unsafe { implicit u =>
      runtime.unsafe.run(zio)
    }
  }

}
