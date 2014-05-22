package etude.app.arrabbiata.ui.pane

import org.controlsfx.control.{NotificationPane => ControlsNotificationPane}
import scalafx.scene.Parent
import org.controlsfx.control.action.AbstractAction
import javafx.event.ActionEvent
import etude.app.arrabbiata.ui.message.LoginShow
import etude.app.arrabbiata.ui.UIActor

case class NotificationPane(parent: Parent) extends ControlsNotificationPane(parent) {
  setText("Notification")
  getActions.add(
    new AbstractAction("Login") {
      def execute(ae: ActionEvent): Unit = {
        UIActor.ui ! LoginShow()
      }
    }
  )
}
