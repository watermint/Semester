package etude.bootstrap

import scala.xml.Elem

object Label {
  def default(v: Any): Elem = <span class="label label-default">{v}</span>
  def primary(v: Any): Elem = <span class="label label-primary">{v}</span>
  def success(v: Any): Elem = <span class="label label-success">{v}</span>
  def info(v: Any): Elem = <span class="label label-info">{v}</span>
  def warning(v: Any): Elem = <span class="label label-warning">{v}</span>
  def danger(v: Any): Elem = <span class="label label-danger">{v}</span>
}
