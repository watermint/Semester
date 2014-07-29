package etude.epice.sel.recipe

import etude.epice.sel.policy.Policy

trait Recipe {
  def withPolicy(policy: Policy): Recipe = ???
}
