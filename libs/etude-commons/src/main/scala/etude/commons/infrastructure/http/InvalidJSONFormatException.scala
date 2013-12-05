package etude.commons.infrastructure.http

case class InvalidJSONFormatException(content: String) extends Exception(content)
