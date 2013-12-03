package etude.http

case class InvalidJSONFormatException(content: String) extends Exception(content)
