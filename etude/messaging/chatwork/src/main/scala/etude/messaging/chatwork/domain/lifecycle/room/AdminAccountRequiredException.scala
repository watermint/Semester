package etude.messaging.chatwork.domain.lifecycle.room

case class AdminAccountRequiredException(message: String) extends Exception(message)
