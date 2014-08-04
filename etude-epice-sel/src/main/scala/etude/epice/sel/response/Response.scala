package etude.epice.sel.response

trait Response {
  def expectXML(): ResponseXML

  def expectJSON(): ResponseJSON
}
