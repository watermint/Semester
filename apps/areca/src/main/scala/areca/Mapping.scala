package areca

trait Mapping {
  val rules: Map[String, Rule]

  lazy val keys: Seq[String] = rules.keys.toSeq

  def rule(key: String): Option[Rule] = rules.get(key)
}
