package etude.bootstrap

import scala.xml.Elem

object Alert {
  def danger(v: Any): Elem = <div class="alert alert-danger">{v}</div>
  def dangerDismissable(v: Any): Elem = <div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>{v}</div>
  def info(v: Any): Elem = <div class="alert alert-info">{v}</div>
  def infoDismissable(v: Any): Elem = <div class="alert alert-info alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>{v}</div>
  def success(v: Any): Elem = <div class="alert alert-success">{v}</div>
  def successDismissable(v: Any): Elem = <div class="alert alert-success alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>{v}</div>
  def warning(v: Any): Elem = <div class="alert alert-warning">{v}</div>
  def warningDismissable(v: Any): Elem = <div class="alert alert-warning alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>{v}</div>
}
