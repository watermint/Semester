package etude.app.arrabbiata.ui

import scalafx.scene.{Parent, Scene}
import scalafx.scene.layout._
import scalafx.scene.control._
import javafx.event.ActionEvent
import org.controlsfx.control.NotificationPane
import org.controlsfx.control.action.AbstractAction
import org.controlsfx.dialog.{AbstractDialogAction, Dialog}
import etude.app.arrabbiata.controller.AppActor
import etude.foundation.logging.LoggerFactory
import etude.app.arrabbiata.ui.message.StatusUpdate
import etude.app.arrabbiata.controller.message.Login

trait Main extends UI {
  val logger = LoggerFactory.getLogger(getClass)

  def loginDialog(): Dialog = {
    val gridPane = new GridPane {
      hgap = 10
      vgap = 10
    }
    val organizationField = new TextField {
      promptText = "Organization ID (leave empty if you are using chatwork.com)"
      hgrow = Priority.ALWAYS
    }
    val usernameField = new TextField {
      promptText = "Email address"
      hgrow = Priority.ALWAYS
    }
    val passwordField = new PasswordField {
      promptText = "Password"
      hgrow = Priority.ALWAYS
    }

    gridPane.add(new Label("Organization"), 0, 0)
    gridPane.add(organizationField, 1, 0)
    gridPane.add(new Label("Email"), 0, 1)
    gridPane.add(usernameField, 1, 1)
    gridPane.add(new Label("Password"), 0, 2)
    gridPane.add(passwordField, 1, 2)

    val dialog = new Dialog(null, "ChatWork Login")
    val loginAction = new AbstractDialogAction("Login") {
      def execute(ae: ActionEvent): Unit = {
        val progressDialog = new Dialog(dialog.getWindow, "Login...")

        AppActor.app ! Login(
          usernameField.text.value,
          passwordField.text.value,
          organizationField.text.value
        )

        progressDialog.show()
      }
    }

    dialog.setContent(gridPane)
    dialog.getActions.addAll(loginAction)

    dialog
  }

  lazy val notification: NotificationPane = {
    val n = new NotificationPane(headerPane())
    n.setText("Notification")
    n.getActions.add(new AbstractAction("Configure") {
      def execute(ae: ActionEvent): Unit = {
        loginDialog().show()
        notification.hide()
      }
    })

    n
  }

  lazy val centerPane = new HBox {
    content = Seq(
      new Button {
        text = "Hello"
        onAction = event {
          e =>
            MainActor.ui ! StatusUpdate("pressed!")
        }
      }
    )
  }

  lazy val notificationPane: BorderPane = {
    val borderPane = new BorderPane()
    borderPane.setCenter(notification)
    borderPane
  }

  def headerPane(): Parent = new ToolBar {
    items = Seq(
      Label("Arrabbiata"),
      new Region {
        hgrow = Priority.ALWAYS
        minWidth = Region.USE_PREF_SIZE
      }
    )
  }

  lazy val footerLabel = new Label("")

  lazy val footerPane: Parent = new ToolBar {
    items = Seq(
      footerLabel
    )
  }

  lazy val rootPane: Parent = new BorderPane {
    top = notificationPane
    center = centerPane
    bottom = footerPane
  }

  lazy val rootScene: Scene = new Scene {
    root = rootPane
  }
}
