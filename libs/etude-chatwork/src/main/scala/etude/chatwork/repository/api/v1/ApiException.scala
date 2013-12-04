package etude.chatwork.repository.api.v1

case class ApiException(message: String,
                             responseMessages: List[String] = List())
  extends Exception((message ++ responseMessages).mkString(","))
