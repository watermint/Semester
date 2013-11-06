package shinsei

import areca.Mapping

object ShinseiMapping extends Mapping {
  val rules = Map(
    "squirrel" -> ShinseiSquirrel
  )
}
