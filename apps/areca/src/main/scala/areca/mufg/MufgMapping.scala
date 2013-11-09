package areca.mufg

import areca.Mapping

object MufgMapping extends Mapping {
  val rules = Map(
    "areca/squirrel" -> MufgSquirrel
  )
}
