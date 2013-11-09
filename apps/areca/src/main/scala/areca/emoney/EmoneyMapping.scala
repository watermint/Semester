package areca.emoney

import areca.Mapping

object EmoneyMapping extends Mapping {
  val rules = Map(
    "areca/squirrel" -> EmoneySquirrel
  )
}
