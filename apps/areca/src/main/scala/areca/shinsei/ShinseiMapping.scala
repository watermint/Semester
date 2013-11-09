package areca.shinsei

import areca.Mapping

object ShinseiMapping extends Mapping {
  val rules = Map(
    "areca/squirrel" -> ShinseiSquirrel
  )
}
