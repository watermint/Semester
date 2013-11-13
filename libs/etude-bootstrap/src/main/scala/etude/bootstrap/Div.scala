package etude.bootstrap

import scala.xml.Elem

object Div {
  lazy val clearfix: Elem = <div class="clearfix"></div>
  def container(v: Any): Elem = <div class="container">{v}</div>
  def jumbotron(v: Any): Elem = <div class="jumbotron">{v}</div>
  def pageHeader(v: Any): Elem = <div class="page-header">{v}</div>
  def row(v: Any): Elem = <div class="row">{v}</div>

}
