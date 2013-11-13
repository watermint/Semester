package etude.bootstrap

import scala.xml.Elem

object Span {
  def badge(v: Any): Elem = <span class="badge">{v}</span>
}
