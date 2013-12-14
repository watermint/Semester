package etude.foundation.http

case class InvalidJSONFormatException(content: String) extends Exception(content)
