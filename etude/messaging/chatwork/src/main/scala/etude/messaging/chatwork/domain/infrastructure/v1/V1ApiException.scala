package etude.messaging.chatwork.domain.infrastructure.v1

case class V1ApiException(message: String,
                             responseMessages: List[String] = List())
  extends Exception((message ++ responseMessages).mkString(","))
