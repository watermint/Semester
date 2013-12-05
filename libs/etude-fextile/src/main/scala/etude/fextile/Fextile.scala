package etude.fextile

import javafx.event.{EventHandler, Event}
import scalafx.scene.control.Label
import scalafx.geometry.Insets

object Fextile {
  def eventHandler[T <: Event](f: T => Unit): EventHandler[T] = new EventHandler[T] {
    def handle(e: T) = f(e)
  }

  class Alert extends Label {
    margin = Insets(0, 0, 20, 0)
  }
  class AlertSuccess extends Alert {
    styleClass = Seq("alert", "alert-success")
  }
  class AlertInfo extends Alert {
    styleClass = Seq("alert", "alert-info")
  }
  class AlertWarning extends Alert {
    styleClass = Seq("alert", "alert-warning")
  }
  class AlertDanger extends Alert {
    styleClass = Seq("alert", "alert-danger")
  }

  class H1 extends Label {
    styleClass = Seq("h1")
  }
  class H2 extends Label {
    styleClass = Seq("h2")
  }
  class H3 extends Label {
    styleClass = Seq("h3")
  }
  class H4 extends Label {
    styleClass = Seq("h4")
  }
  class H5 extends Label {
    styleClass = Seq("h5")
  }
  class H6 extends Label {
    styleClass = Seq("h6")
  }

}
