package etude.chatwork

case class ExceedQuotaException(message: String) extends RuntimeException(message)