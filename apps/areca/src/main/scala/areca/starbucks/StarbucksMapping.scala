package areca.starbucks

import areca.Mapping

object StarbucksMapping extends Mapping {
  val rules = Map(
    "areca/squirrel" -> StarbucksSquirrel
  )
}
