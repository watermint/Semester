package etude.bookmark.pocket.domain.infrastructure.auth

import com.twitter.finatra.Controller
import com.twitter.logging.Logger
import scala.util.Success
import org.jboss.netty.handler.codec.http.DefaultCookie

class AuthController extends Controller {
  val logger = Logger.get(this.getClass)

  get("/") {
    request =>
      AuthService.acquireCode() match {
        case Success(code) =>
          val cookieForCode = new DefaultCookie("code", code)
          cookieForCode.setSecure(true)
          cookieForCode.setMaxAge(600) // seconds

          render
            .cookie(cookieForCode)
            .html(
              """
                |<body>
                |<h1>Issue New Token</h1>
                |<a href="/auth/request">Connect with Pocket</a>.
                |</body>
              """.stripMargin).
            toFuture

        case _ =>
          render.html(
            """
              |<body>
              |Failed to start authorization process with Pocket.
              |</body>
            """.stripMargin).toFuture
      }

  }

  get("/auth/request") {
    request =>
      request.cookies.get("code") match {
        case Some(code) =>
          redirect(AuthService.redirectUri(code.value).toString).toFuture
        case _ =>
          redirect("/").toFuture
      }
  }

  get("/auth/callback") {
    request =>
      request.cookies.get("code") match {
        case None =>
          redirect("/").toFuture
        case Some(code) =>
          AuthService.authorize(code.value) match {
            case Success(session) =>
              AuthSession.storeSession(session)

              render.html(
                """
                  |<body>
                  |Authorization succeed. Session stored on your home directory.
                  |</body>
                """.stripMargin
              ).toFuture
            case _ =>
              render.html(
                """
                  |<body>
                  |Failed authorization with Pocket.
                  |</body>
                """.stripMargin)
                .toFuture
          }
      }
  }
}
