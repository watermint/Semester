package etude.gazpacho.http

case class InvalidJSONFormatException(content: String) extends Exception(content)
