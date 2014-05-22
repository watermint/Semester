package etude.app.arrabbiata.ui.pane

import org.controlsfx.control.{NotificationPane => ControlsNotificationPane}
import scalafx.scene.Parent

case class NotificationPane(parent: Parent) extends ControlsNotificationPane(parent) {
  setText("Notification")
}
