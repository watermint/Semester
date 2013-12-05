package stenographer.views.admin

import stenographer.App
import etude.bootstrap.{Alert, Form, Html}
import scala.xml.Elem
import etude.commons.utility.html.Csrf

object Login {
  def loginContainer(v: Any): Elem = <div class="col-md-6 col-md-offset-3">{v}</div>

  def loginMenu(csrf: Csrf, errorMessage: Option[String] = None): String =
    App.adminHtml(
      title = "Admin: Connect to Chatwork",
      v = loginContainer(
        List(
          Html.h2("Connect to Chatwork"),
          loginWarning(errorMessage),
          loginForm(csrf)
        )
      )
    )

  def loginForm(csrf: Csrf): Any = {
    Form.form(
      action = "/admin/auth",
      csrfToken = csrf.token,
      v = List(
        Form.formGroup(
          Form.inputText(name = "orgId", placeholder = "Organization Id (blank if you are using chatwork.com)")
        ),
        Form.formGroup(
          Form.inputEmail(name = "email", placeholder = "Email")
        ),
        Form.formGroup(
          Form.inputPassword(name = "password", placeholder = "Password")
        ),
        Form.formGroup(
          <button type="submit" class="btn btn-default">Connect</button>
        )
      )
    )
  }

  def loginWarning(errorMessage: Option[String]): Any = {
    val warn = Alert.warningDismissable(
      """
        |Note: Stenographer records your email/password on memory but not encrypted.
        |Someone, who can attach debugger to this process, could read these data.
      """.stripMargin.trim)

    errorMessage match {
      case Some(m) => List(Alert.danger(m), warn)
      case _ => warn
    }
  }
}
