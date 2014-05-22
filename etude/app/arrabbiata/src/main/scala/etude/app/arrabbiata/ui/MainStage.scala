package etude.app.arrabbiata.ui

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import etude.app.arrabbiata.ui.message.{StatusUpdate, NotificationShow}

object MainStage extends JFXApp with Main {
  def updateFooter(status: StatusUpdate): Unit = {
    footerLabel.text = status.status
  }

  def notify(text: NotificationShow): Unit = {
    notification.show()
  }

  def hideNotification(): Unit = {
    notification.hide()
  }

  stage = new PrimaryStage {
    scene = rootScene
    width = 800
    height = 600
  }
}
