package suruga

import areca.Mapping

object SurugaMapping extends Mapping {
  val rules = Map(
    "squirrel" -> SurugaSquirrel
  )
}
