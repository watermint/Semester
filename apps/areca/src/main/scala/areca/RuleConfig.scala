package areca

import org.slf4j.{LoggerFactory, Logger}
import scala.collection.JavaConversions._

object RuleConfig {
  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def getBoolean(configKey: String): Boolean = Main.config.getBoolean(configKey)

  def getString(configKey: String): String = Main.config.getString(configKey)

  def getList(configKey: String): Seq[String] = {
    Main.config.getStringList(configKey).toList
  }

  def getMap(configKey: String): Map[String, String] = {
    Main.config
      .getObject(configKey)
      .entrySet
      .toList
      .map(e => e.getKey -> e.getValue.unwrapped.asInstanceOf[String])
      .toMap
  }

  def solve(text: String, rule: Map[String, String]): Option[String] = {
    rule.keys.find(text.contains(_)) match {
      case Some(r) => rule.get(r)
      case _ => None
    }
  }
}
