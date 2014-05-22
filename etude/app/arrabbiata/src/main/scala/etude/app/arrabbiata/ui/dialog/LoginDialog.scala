package etude.app.arrabbiata.ui.dialog

import javafx.event.ActionEvent
import scalafx.stage.Window
import scalafx.scene.layout.{Priority, GridPane}
import scalafx.scene.control.{Label, PasswordField, TextField}
import org.controlsfx.dialog.{AbstractDialogAction, Dialog}
import etude.app.arrabbiata.controller.AppActor
import etude.app.arrabbiata.controller.message.Login

case class LoginDialog(parent: Object) extends Dialog(parent, "ChatWork Login") {
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

  val loginAction = new AbstractDialogAction("Login") {
    def execute(ae: ActionEvent): Unit = {
      AppActor.app ! Login(
        usernameField.text.value,
        passwordField.text.value,
        organizationField.text.value
      )
    }
  }

  setContent(gridPane)
  getActions.addAll(loginAction)
}
