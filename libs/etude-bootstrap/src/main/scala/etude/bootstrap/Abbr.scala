package etude.bootstrap

import scala.xml.Elem

object Abbr {
  def initialism(title: String, v: Any): Elem = <abbr title={title} class="initialism">{v}</abbr>
}
