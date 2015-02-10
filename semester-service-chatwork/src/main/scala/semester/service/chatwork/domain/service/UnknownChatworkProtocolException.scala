package semester.service.chatwork.domain.service

case class UnknownChatworkProtocolException(message: String, payload: Option[String] = None) extends Exception(message)
