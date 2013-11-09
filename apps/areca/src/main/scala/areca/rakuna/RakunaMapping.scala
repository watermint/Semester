package areca.rakuna

import areca.Mapping

object RakunaMapping extends Mapping {
  val rules = Map(
    "areca/squirrel" -> RakunaSquirrel
  )
}
