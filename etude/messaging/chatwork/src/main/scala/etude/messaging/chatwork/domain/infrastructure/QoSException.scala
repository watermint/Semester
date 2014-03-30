package etude.messaging.chatwork.domain.infrastructure

case class QoSException(message: String) extends Exception(message)
