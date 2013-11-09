package areca.squirrel

import areca.{RuleConfig, Main}

case class SquirrelCategory(name: String)

/**
 * Squirrelに定義しておいたカテゴリ.
 */
object SquirrelCategory {
  lazy val defaultCategory = SquirrelCategory(RuleConfig.getString("areca.squirrel.defaultCategory"))

  lazy val categories: Map[String, SquirrelCategory] = {
    RuleConfig.getMap("areca.squirrel.category").map(e => e._1 -> SquirrelCategory(e._2))
  }

  def normalize(category: String): SquirrelCategory = {
    categories.find(_._2.name.equals(category)) match {
      case Some(c) => c._2
      case _ => defaultCategory
    }
  }

  def category(name: String): SquirrelCategory = SquirrelCategory(RuleConfig.getString("areca.squirrel.category." + name))

  def fromTextRule(text: String, ruleName: String, defaultCategoryName: Option[String] = None): SquirrelCategory = {
    RuleConfig.solve(text, RuleConfig.getMap(ruleName)) match {
      case Some(c) => SquirrelCategory.category(c)
      case _ => defaultCategoryName match {
        case Some(name) => SquirrelCategory(RuleConfig.getString("areca.squirrel.category." + name))
        case _ => defaultCategory
      }
    }
  }
}
