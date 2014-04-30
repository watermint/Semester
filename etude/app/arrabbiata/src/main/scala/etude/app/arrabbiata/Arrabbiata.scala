package etude.app.arrabbiata

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{Parent, Scene}
import scalafx.scene.layout._
import scalafx.scene.control._
import org.controlsfx.control.NotificationPane
import javafx.event.{ActionEvent, EventHandler, Event}
import org.controlsfx.control.action.AbstractAction
import org.controlsfx.dialog.{AbstractDialogAction, Dialog}

object Arrabbiata extends JFXApp {
  def event[T <: Event](f: T => Unit): EventHandler[T] = new EventHandler[T] {
    def handle(e: T) = f(e)
  }

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

    val dialog = new Dialog(null, "Login")
    dialog.setContent(gridPane)
    dialog.getActions.addAll(
      new AbstractDialogAction("Login") {
        def execute(ae: ActionEvent): Unit = {
        }
      },
      Dialog.Actions.CANCEL
    )

    dialog
  }

  val notification: NotificationPane = {
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

  val centerPane = new HBox {
    content = new Button {
      text = "Show"
      onAction = event {
        e =>
          notification.show()
      }
    }
  }

  val notificationPane: BorderPane = {
    val borderPane = new BorderPane()
    borderPane.setCenter(notification)
    borderPane
  }

  def headerPane(): Parent = new ToolBar {
    items = Seq(
      Label("Fedelini"),
      new Region {
        hgrow = Priority.ALWAYS
        minWidth = Region.USE_PREF_SIZE
      },
      new Button {
        text = "Login"
        onAction = event {
          e =>
            loginDialog().show()
        }
      }
    )
  }

  def footerPane(): Parent = new ToolBar {
    items = Seq(
      new ProgressIndicator {
        prefWidth = 12
        prefHeight = 12
      },
      new Label("Loading...")
    )
  }

  def rootPane(): Parent = new BorderPane {
    top = notificationPane
    center = centerPane
    bottom = footerPane()
  }

  def rootScene(): Scene = new Scene {
    root = rootPane()
  }

  stage = new PrimaryStage {
    scene = rootScene()
    width = 800
    height = 600
  }
}
