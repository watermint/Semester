package semester.foundation.fextile.stage

import javafx.{stage => fxs}

import semester.foundation.fextile.boundary.FextileDelegate
import semester.foundation.fextile.scene.Scene

import scala.concurrent.Future

class Stage
  extends Window
  with FextileDelegate[fxs.Stage] {

  override def createDelegate: fxs.Stage = new fxs.Stage()

  def title: Future[String] = delegate(_.getTitle)

  def title_=(t: String): Unit = delegate(_.setTitle(t))

  private var _scene: Option[Scene] = None

  def scene: Scene = _scene.get

  def scene_=(s: Scene) = {
    _scene = Some(s)
    s.eventSupervisor = Some(this)
    s.delegate map {
      case _s =>
        delegate(_.setScene(_s))
    }
  }
}
