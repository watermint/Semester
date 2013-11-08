package emoney

import areca.Mapping

object EmoneyMapping extends Mapping {
  val rules = Map(
    "squirrel" -> EmoneySquirrel
  )
}
