package semester.foundation.utilities.helpers

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object FutureHelper {
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
