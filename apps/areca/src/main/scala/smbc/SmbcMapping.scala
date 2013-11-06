package smbc

import areca.Mapping

object SmbcMapping extends Mapping {
  val rules = Map(
    "squirrel" -> SmbcSquirrel
  )
}
