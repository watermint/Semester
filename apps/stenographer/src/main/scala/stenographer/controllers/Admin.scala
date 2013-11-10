package stenographer.controllers

import com.twitter.finatra.Controller
import stenographer.View
import stenographer.models.Connect

class Admin extends Controller {
  get("/admin") {
    request =>
      render
        .html(Connect.currentSession match {
        case Some(s) => View.skeleton("Admin Menu", adminMenu(), Some("admin"))
        case _ => View.skeleton("Admin Login", loginForm(), Some("admin"))
      }).toFuture
  }

  post("/admin/auth") {
    request => {
      val orgId = request.params.get("orgId")
      val email = request.params.get("email")
      val password = request.params.get("password")

      (email, password, orgId) match {
        case (Some(e), Some(p), Some(o)) => {
          Connect.login(e, p,
            if (o == "") {
              None
            } else {
              Some(o)
            }
          ) match {
            case Right(s) =>
              render
                .plain("")
                .status(302)
                .header("Location", "/admin")
                .toFuture

            case Left(x) =>
              render
                .html(View.skeleton(
                "Admin Login",
                loginForm(
                  <div class="alert alert-danger">Login failed with message:
                    {x.getMessage}
                  </div>),
                Some("admin")))
                .toFuture
          }
        }
        case _ =>
          render
            .html(View.skeleton(
            "Admin Login",
            loginForm(<div class="alert alert-danger">Please enter email and/or password.</div>),
            Some("admin")))
            .toFuture
      }
    }
  }

  def adminMenu(): Any = View.divContainer(
    List(
      <div class="page-header">
        <h1>Connect to ChatWork</h1>
      </div>
    ),
    Some("col-sm-6 col-sm-offset-3 col-md-6 col-md-offset-3 col-lg-4 col-lg-offset-4")
  )

  def loginForm(message: Any = ""): Any = View.divContainer(
    List(
      <div class="page-header">
        <h1>Connect to ChatWork</h1>
      </div>,
      message,
      <div class="alert alert-warning">
        Note: Stenographer records your email/password on memory but not encrypted.
        Someone, who can attach debugger to this process, could read these data.
      </div>,
      <form class="form" role="form" method="post" action="/admin/auth">
        <div class="form-group">
          <input name="orgId" type="text" class="form-control" placeholder="Organization Id (blank if you are using chatwork.com)"/>
        </div>
        <div class="form-group">
          <input name="email" type="email" class="form-control" placeholder="Email"/>
        </div>
        <div class="form-group">
          <input name="password" type="password" class="form-control" placeholder="Password"/>
        </div>
        <div class="form-group">
          <button type="submit" class="btn btn-default">Login</button>
        </div>
      </form>
    ),
    Some("col-sm-6 col-sm-offset-3 col-md-6 col-md-offset-3 col-lg-4 col-lg-offset-4")
  )
}
