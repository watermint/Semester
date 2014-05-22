package etude.app.arrabbiata.ui.pane

import scalafx.scene.layout.HBox
import scalafx.scene.control.Button
import etude.app.arrabbiata.ui.{UI, UIActor}
import etude.app.arrabbiata.ui.message.{NotificationShow, LoginShow, StatusUpdate}
import etude.foundation.logging.LoggerFactory

case class CenterPane() extends HBox with UI {
  val logger = LoggerFactory.getLogger(getClass)

  content = Seq(
    new Button {
      text = "Update footer"
      onAction = event {
        e =>
          logger.info("update footer")
          UIActor.ui ! StatusUpdate("pressed!")
      }
    },
    new Button {
      text = "Login"
      onAction = event {
        e =>
          logger.info("login show")
          UIActor.ui ! LoginShow()
      }
    },
    new Button {
      text = "Notification"
      onAction = event {
        e =>
          UIActor.ui ! NotificationShow("Hello")
      }
    }
  )

}
