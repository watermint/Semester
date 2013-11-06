package saison

import areca.Mapping

object SaisonMapping extends Mapping {
  val rules = Map(
    "squirrel" -> SaisonSquirrel
  )
}
