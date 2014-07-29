package etude.epice.sel.request

case class Verb(verb: String) {
}

object Verb {
  val GET = Verb("GET")
  val POST = Verb("POST")
  val PUT = Verb("PUT")
  val DELETE = Verb("DELETE")
}
