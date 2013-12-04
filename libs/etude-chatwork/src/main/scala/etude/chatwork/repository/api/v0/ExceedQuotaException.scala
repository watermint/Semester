package etude.chatwork.repository.api.v0

case class ExceedQuotaException(message: String) extends RuntimeException(message)