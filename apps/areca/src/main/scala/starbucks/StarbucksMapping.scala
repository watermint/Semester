package starbucks

import areca.Mapping

object StarbucksMapping extends Mapping {
  val rules = Map(
    "squirrel" -> StarbucksSquirrel
  )
}
