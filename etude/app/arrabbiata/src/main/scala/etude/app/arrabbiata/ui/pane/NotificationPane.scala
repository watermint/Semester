package etude.app.arrabbiata.ui.pane

import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.micro.LoginShow
import org.controlsfx.control.action.{ActionMap, ActionProxy}
import org.controlsfx.control.{NotificationPane => ControlsNotificationPane}

import scalafx.scene.Parent

case class NotificationPane(parent: Parent) extends ControlsNotificationPane(parent) {
  ActionMap.register(this)
  setText("Notification")
  getActions.add(ActionMap.action("login"))

  @ActionProxy(text="Login")
  def login(): Unit = {
    UIActor.ui ! LoginShow()
  }
}
