package etude.gazpacho.utility

import java.util.Properties

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

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

  def unwrapTry[T](t: Try[T]): T = {
    t match {
      case Success(s) => s
      case Failure(e) => throw e
    }
  }

  def propertiesToMap(p: Properties): Map[String, String] = {
    p.entrySet().asScala.map {
      e =>
        e.getKey.toString -> e.getValue.toString
    }.toMap
  }
}
