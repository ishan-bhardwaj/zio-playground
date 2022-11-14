package io.laymen
package io.laymen.playground.zio.effects

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Effects {

  /**
   * Effect is a data type having following properties -
   * - Type signature describes what kind of computation it will perform
   * - Type signature describes what type of value it will produce
   * - If side effects are required, construction must be separate from the execution
   */

  /**
   * Example 1 - Option is an effect
   * - Type signature describes kind of computation = a possibly absent value
   * - Type signature says that the computation returns an A, if the computation does produce something
   * - No side effects are needed
   */
  val anOption: Option[Int] = Option(42)

  /**
   * Example - Future is NOT an effect
   * - Type signature describes kind of computation = asynchronous computation
   * - Type signature says that the computation returns an A, if the computation finishes and it's successful
   * - Side effects are required - allocating the JVM thread and scheduling the computation on it
   *   therefore, construction is not separate from execution.
   */
  val aFuture: Future[Int] = Future(42)

}
