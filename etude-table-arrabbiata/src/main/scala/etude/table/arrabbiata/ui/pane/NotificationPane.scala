package etude.table.arrabbiata.ui.pane

import etude.table.arrabbiata.ui.UIActor
import etude.table.arrabbiata.ui.message.micro.session.LoginShow
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
