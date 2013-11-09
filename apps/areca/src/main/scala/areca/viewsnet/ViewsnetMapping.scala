package areca.viewsnet

import areca.Mapping

object ViewsnetMapping extends Mapping {
  val rules = Map(
    "areca/squirrel" -> ViewsnetSquirrel
  )
}
