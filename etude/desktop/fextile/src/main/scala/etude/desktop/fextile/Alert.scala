package etude.desktop.fextile

import scalafx.geometry.Insets
import scalafx.scene.control.Label

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
