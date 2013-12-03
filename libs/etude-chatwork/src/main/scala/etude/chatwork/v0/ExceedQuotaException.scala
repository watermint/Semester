package etude.chatwork.v0

case class ExceedQuotaException(message: String) extends RuntimeException(message)