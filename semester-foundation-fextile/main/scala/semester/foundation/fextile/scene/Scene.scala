package semester.foundation.fextile.scene

import javafx.scene.paint.Paint
import javafx.{scene => fxs}
import javafx.scene.{input => fxsi}

import semester.foundation.fextile.boundary.FextileDelegate
import semester.foundation.fextile.event._

import scala.concurrent.Future

class Scene extends FextileDelegate[fxs.Scene] {
  override protected def createDelegate: fxs.Scene = {
    new fxs.Scene(new fxs.Group())
  }

  override protected def decorateDelegate[DD >: fxs.Scene](target: DD): Unit = {
    super.decorateDelegate(target)
    target match {
      case t: fxs.Scene =>
        t.setOnMouseClicked(handler[fxsi.MouseEvent](MouseClicked))
        t.setOnDragDetected(handler[fxsi.MouseEvent](MouseDragDetected))
        t.setOnMouseDragged(handler[fxsi.MouseEvent](MouseDragged))
        t.setOnMouseEntered(handler[fxsi.MouseEvent](MouseEntered))
        t.setOnMouseExited(handler[fxsi.MouseEvent](MouseExited))
        t.setOnMouseMoved(handler[fxsi.MouseEvent](MouseMoved))
    }
  }

  def fill_=(f: Paint) = delegate(_.setFill(f))
  def fill: Future[Paint] = delegate(_.getFill)
}
