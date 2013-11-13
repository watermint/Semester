package stenographer.models

case class InvalidLoginException(message: String) extends RuntimeException(message)
