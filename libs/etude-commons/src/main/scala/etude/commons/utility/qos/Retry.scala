package etude.commons.utility.qos

/**
 * forked from: https://gist.github.com/xuwei-k/2996927
 * TODO: consider impl. with util.Try like: http://stackoverflow.com/questions/7930814/whats-the-scala-way-to-implement-a-retry-able-call-like-this-one
 */
object Retry {
  case class RetryException(causes: List[Throwable]) extends Exception

  def retry[T](retries: Int, shouldCatch: Throwable => Boolean = _ => true)(f: => T): T = {
    def doTry(causes: List[Throwable], f: => T): T = {
      try {
        f
      } catch {
        case e if shouldCatch(e) =>
          if (causes.size < retries) {
            doTry(e :: causes, f)
          } else {
            throw RetryException(causes)
          }
      }
    }
    doTry(Nil, f)
  }
}
