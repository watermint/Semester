package etude.epice.sel.client

import etude.epice.sel.recipe.Recipe
import etude.epice.sel.request.{Verb, Request}
import etude.epice.sel.signature.Signer

trait Client {
  val context: Context

  def currentSession(): Session = ???
}

object Client {
  def of(recipe: Recipe): Client = ???
}