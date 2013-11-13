package etude.bootstrap

import scala.xml.Elem

object Panel {
  def default(v: Any): Elem = <div class="panel panel-default">{v}</div>
  def heading(v: Any): Elem = <div class="panel-heading">{v}</div>
  def body(v: Any): Elem = <div class="panel-body">{v}</div>
  def titleH1(v: Any): Elem = <h1 class="panel-title">{v}</h1>
  def titleH2(v: Any): Elem = <h2 class="panel-title">{v}</h2>
  def titleH3(v: Any): Elem = <h3 class="panel-title">{v}</h3>
  def titleH4(v: Any): Elem = <h4 class="panel-title">{v}</h4>
  def titleH5(v: Any): Elem = <h5 class="panel-title">{v}</h5>
  def titleH6(v: Any): Elem = <h6 class="panel-title">{v}</h6>
  def footer(v: Any): Elem = <div class="panel-footer">{v}</div>
  def primary(v: Any): Elem = <div class="panel panel-primary">{v}</div>
  def success(v: Any): Elem = <div class="panel panel-success">{v}</div>
  def info(v: Any): Elem = <div class="panel panel-info">{v}</div>
  def warning(v: Any): Elem = <div class="panel panel-warning">{v}</div>
  def danger(v: Any): Elem = <div class="panel panel-danger">{v}</div>
}
