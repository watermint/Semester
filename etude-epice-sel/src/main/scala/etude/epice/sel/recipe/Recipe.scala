package etude.epice.sel.recipe

import etude.epice.sel.client.{Session, Client}
import etude.epice.sel.policy.Policy

trait Recipe {
  def withPolicy(policy: Policy): Recipe = ???

  def ofSession[S <: Session[_, _]](client: Client): S = ???
}
