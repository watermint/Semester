package etude.epice.http

case class InvalidJSONFormatException(content: String) extends Exception(content)
