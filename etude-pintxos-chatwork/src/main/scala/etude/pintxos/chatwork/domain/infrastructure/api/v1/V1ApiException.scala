package etude.pintxos.chatwork.domain.infrastructure.api.v1

case class V1ApiException(message: String,
                             responseMessages: List[String] = List())
  extends Exception((message ++ responseMessages).mkString(","))
