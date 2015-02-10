package semester.foundation.logging

import org.slf4j.{Logger => Slf4jLogger, LoggerFactory => Slf4jLoggerFactory}

case class Logger(clazz: Class[_]) {
  private val logger: Slf4jLogger = Slf4jLoggerFactory.getLogger(clazz)

  def trace(msg: => String): Unit = if (isTraceEnabled) { logger.trace(msg) }
  def trace(msg: => String, t: Throwable): Unit = if (isTraceEnabled) { logger.trace(msg, t) }
  def trace(msg: => String, arg: Array[Object]): Unit = if (isTraceEnabled) { logger.trace(msg, arg) }
  def debug(msg: => String): Unit = if (isDebugEnabled) { logger.debug(msg) }
  def debug(msg: => String, t: Throwable): Unit = if (isDebugEnabled) { logger.debug(msg, t) }
  def debug(msg: => String, arg: Array[Object]): Unit = if (isDebugEnabled) { logger.debug(msg, arg) }
  def info(msg: => String): Unit = if (isInfoEnabled) { logger.info(msg) }
  def info(msg: => String, t: Throwable): Unit = if (isInfoEnabled) { logger.info(msg, t) }
  def info(msg: => String, arg: Array[Object]): Unit = if (isInfoEnabled) { logger.info(msg, arg) }
  def warn(msg: => String): Unit = if (isWarnEnabled) { logger.warn(msg) }
  def warn(msg: => String, t: Throwable): Unit = if (isWarnEnabled) { logger.warn(msg, t) }
  def warn(msg: => String, arg: Array[Object]): Unit = if (isWarnEnabled) { logger.warn(msg, arg) }
  def error(msg: => String): Unit = if (isErrorEnabled) { logger.error(msg) }
  def error(msg: => String, t: Throwable): Unit = if (isErrorEnabled) { logger.error(msg, t) }
  def error(msg: => String, arg: Array[Object]): Unit = if (isErrorEnabled) { logger.error(msg, arg) }

  val isTraceEnabled: Boolean = logger.isTraceEnabled
  val isDebugEnabled: Boolean = logger.isDebugEnabled
  val isInfoEnabled: Boolean = logger.isInfoEnabled
  val isWarnEnabled: Boolean = logger.isWarnEnabled
  val isErrorEnabled: Boolean = logger.isErrorEnabled
}
