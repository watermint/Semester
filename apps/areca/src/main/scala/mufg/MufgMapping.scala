package mufg

import areca.Mapping

object MufgMapping extends Mapping {
  val rules = Map(
    "squirrel" -> MufgSquirrel
  )
}
