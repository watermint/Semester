package etude.table.arrabbiata.ui

import etude.table.arrabbiata.controller.AppActor
import etude.table.arrabbiata.controller.message.session.LoadSession
import etude.table.arrabbiata.ui.dialog.LoginDialog
import etude.table.arrabbiata.ui.pane.{CenterPane, FooterPane, HeaderPane, NotificationPane}
import etude.gazpacho.logging.LoggerFactory

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.layout._
import scalafx.scene.{Parent, Scene}

object Main extends JFXApp with UI {
  AppActor.app ! LoadSession()

  val logger = LoggerFactory.getLogger(getClass)

  lazy val loginDialog = LoginDialog(rootScene.getWindow)

  lazy val notificationPane = NotificationPane(headerPane)

  lazy val centerPane = CenterPane

  lazy val headerPane = HeaderPane

  lazy val footerPane = FooterPane

  lazy val notificationPaneContainer: BorderPane = {
    val borderPane = new BorderPane()
    borderPane.setTop(menuBar)
    borderPane.setCenter(notificationPane)
    borderPane
  }

  lazy val rootPane: Parent = new BorderPane {
    top = notificationPaneContainer
    center = centerPane
    bottom = footerPane
  }

  lazy val rootScene: Scene = new Scene {
    root = rootPane
  }

  lazy val menuBar = MainMenu()

  stage = new PrimaryStage {
    title = "Arrabbiata"
    scene = rootScene
    width = 800
    height = 600
    onCloseRequest = event {
      e =>
        UIActor.system.shutdown()
        AppActor.system.shutdown()
        System.exit(0)
    }
  }
}
