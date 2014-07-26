package etude.epice.utility.helpers

import java.util.Properties
import scala.collection.JavaConverters._

object PropertiesHelper {
  def propertiesToMap(p: Properties): Map[String, String] = {
    p.entrySet().asScala.map {
      e =>
        e.getKey.toString -> e.getValue.toString
    }.toMap
  }
}
