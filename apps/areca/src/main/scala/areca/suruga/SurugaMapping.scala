package areca.suruga

import areca.Mapping

object SurugaMapping extends Mapping {
  val rules = Map(
    "areca/squirrel" -> SurugaSquirrel
  )
}
