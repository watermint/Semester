package areca.smbc

import areca.Mapping

object SmbcMapping extends Mapping {
  val rules = Map(
    "areca/squirrel" -> SmbcSquirrel
  )
}
