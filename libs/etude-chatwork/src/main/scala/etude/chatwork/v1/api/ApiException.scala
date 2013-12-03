package etude.chatwork.v1.api

case class ApiException(message: String,
                             responseMessages: List[String] = List())
  extends Exception((message ++ responseMessages).mkString(","))
