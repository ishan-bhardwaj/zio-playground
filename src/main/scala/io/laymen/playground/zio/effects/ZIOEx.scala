package io.laymen
package io.laymen.playground.zio.effects

import zio._

object ZIOEx {

  /**
   * [Imp] ZIO flatMaps implement a technique called trampoline which means allocating zio instances on the heap
   * instead of stack. So, zio flatMap returns immediately with new data structure and when the final program
   * evaluates the final zio data structure, evaluating this chain is done in a tail recursive fashion behind
   * the scenes by zio runtime.
   */

  /**
   * 1. Sequence two ZIOs and take the value of the last one.
   * First evaluate zioA and then evaluate zioB.
   * Finally, return the value of zioB.
   */
  def sequenceTakeLast[R, E, A, B](zioA: ZIO[R, E, A], zioB: ZIO[R, E, B]): ZIO[R, E, B] = {
    for {
      _ <- zioA
      b <- zioB
    } yield b
  }

  // ZIO library version of sequenceTakeLast
  def sequenceTakeLast_v2[R, E, A, B](zioA: ZIO[R, E, A], zioB: ZIO[R, E, B]): ZIO[R, E, B] =
    zioA *> zioB

  /**
   * 2. Sequence two ZIOs and take the value of the last one.
   * First evaluate zioA and then evaluate zioB.
   * Finally, return the value of zioA.
   */
  def sequenceTakeFirst[R, E, A, B](zioA: ZIO[R, E, A], zioB: ZIO[R, E, B]): ZIO[R, E, A] = {
    for {
      a <- zioA
      _ <- zioB
    } yield a
  }

  // ZIO library version of sequenceTakeFirst
  def sequenceTakeFirst_v2[R, E, A, B](zioA: ZIO[R, E, A], zioB: ZIO[R, E, B]): ZIO[R, E, A] =
    zioA <* zioB

  /**
   * 3. Run a ZIO forever.
   */
  def runForever[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, A] = {
    zio.flatMap(_ => runForever(zio))
  }

  // ZIO library version of runForever
  def runForever_v2[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, A] = {
    zio *> runForever_v2(zio)
  }

  /**
   * 4. Convert the value of ZIO to something else.
   * Runs the zio and when completed, return the value (the second argument).
   */
  def convert[R, E, A, B](zio: ZIO[R, E, A], value: B): ZIO[R, E, B] = {
    zio.map(_ => value)
  }

  // ZIO library version of convert
  def convert_v2[R, E, A, B](zio: ZIO[R, E, A], value: B): ZIO[R, E, B] =
    zio.as(value)

  /**
   * 5. Discard the value of ZIO to Unit.
   */
  def asUnit[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, Unit] = {
    zio.map(_ => ())
  }

  // ZIO library version of asUnit
  def asUnit_v2[R, E, A, B](zio: ZIO[R, E, A], value: B): ZIO[R, E, B] =
    zio.as(value)

  /**
   * 6. Find sum of numbers till n using recursion.
   */
  def sumZIO(n: Int): UIO[Int] = {
    if (n == 0) ZIO.succeed(0)
    else for {
      current <- ZIO.succeed(n)
      prevSum <- sumZIO(n - 1)
    } yield current + prevSum
  }

  /**
   * 7. Fibonacci sequence
   * Hint: use ZIO.suspend to resolve stackoverflow exceptions
   */
  def fibonacciZIO(n: Int): UIO[Int] = {
    /**
     * This implementation will fail with stack overflow if fibonacciZIO(n - 1) is used for prevSum calculation
     * because the chains of prevSum has to be evaluated first before the rest of the code and hence, it throws
     * the stackoverflow. Therefore, we need to "delay" the evaluation of prevSum which can be done using
     * ZIO.suspendSucceed() (suspend version which discards the error channel)
     */
    if (n <= 2) ZIO.succeed(1)
    else for {
      prevSum <- ZIO.suspendSucceed(fibonacciZIO(n - 1)) // fibonacciZIO(n - 1) will fail with stackoverflow
      prevToPrevSum <- fibonacciZIO(n - 2)
    } yield prevSum + prevToPrevSum
  }

  def main(args: Array[String]): Unit = {
    val runtime = Runtime.default
    implicit val trace: Trace = Trace.empty

    // Exercise 1
    println("Testing exercise 1 ==>")
    Unsafe.unsafeCompat { implicit u =>
      val zioA = ZIO.succeed {
        println("Computing first effect")
        1
      }

      val zioB = ZIO.succeed {
        println("Computing second effect")
        2
      }

      println(runtime.unsafe.run(sequenceTakeLast(zioA, zioB))) // prints 2
    }

    // Exercise 2
    println("Testing exercise 2 ==>")
    Unsafe.unsafeCompat { implicit u =>
      val zioA = ZIO.succeed {
        println("Computing first effect")
        1
      }

      val zioB = ZIO.succeed {
        println("Computing second effect")
        2
      }

      println(runtime.unsafe.run(sequenceTakeFirst(zioA, zioB))) // prints 1
    }

    // Exercise 3
    println("Testing exercise 3 ==>")
    /*Unsafe.unsafeCompat { implicit u =>
      val endlessLoop = runForever {
        ZIO.succeed {
          println("running.....")
          Thread.sleep(1000)
        }
      }

      runtime.unsafe.run(endlessLoop)
    }*/

    // Exercise 6
    println("Testing exercise 6 ==>")
    Unsafe.unsafeCompat { implicit u =>
      println(runtime.unsafe.run(sumZIO(20000))) // Will use trampoline technique, hence will succeed without any stackoverflow
    }
  }

}
