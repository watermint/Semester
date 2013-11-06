package viewsnet

import areca.Mapping

object ViewsnetMapping extends Mapping {
  val rules = Map(
    "squirrel" -> ViewsnetSquirrel
  )
}
