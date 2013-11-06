package rakuna

import areca.Mapping

/**
 *
 */
object RakunaMapping extends Mapping {
  val rules = Map(
    "squirrel" -> RakunaSquirrel
  )
}
