package etude.foundation.utility

import scala.concurrent.Future
import scala.util.{Try, Success, Failure}

object Converter {
  /**
   * @param t value of try
   * @tparam T type
   * @return future
   * @see http://stackoverflow.com/questions/17907772/scala-chaining-futures-try-blocks
   */
  def tryToFuture[T](t: Try[T]): Future[T] = {
    t match{
      case Success(s) => Future.successful(s)
      case Failure(ex) => Future.failed(ex)
    }
  }
}
