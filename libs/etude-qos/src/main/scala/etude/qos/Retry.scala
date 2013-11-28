package etude.qos

/**
 * forked from: https://gist.github.com/xuwei-k/2996927
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
