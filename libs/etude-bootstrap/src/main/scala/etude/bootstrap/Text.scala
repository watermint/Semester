package etude.bootstrap

import scala.xml.Elem

object Text {
  def danger(v: Any): Elem = <div class="text-danger">{v}</div>
  def info(v: Any): Elem = <div class="text-info">{v}</div>
  def muted(v: Any): Elem = <div class="text-muted">{v}</div>
  def primary(v: Any): Elem = <div class="text-primary">{v}</div>
  def success(v: Any): Elem = <div class="text-success">{v}</div>
  def warning(v: Any): Elem = <div class="text-warning">{v}</div>
  def well(v: Any): Elem = <div class="well">{v}</div>
  def wellLarge(v: Any): Elem = <div class="well well-lg">{v}</div>
  def wellSmall(v: Any): Elem = <div class="well well-sm">{v}</div>
}