package areca.saison

import areca.Mapping

object SaisonMapping extends Mapping {
  val rules = Map(
    "areca/squirrel" -> SaisonSquirrel
  )
}
