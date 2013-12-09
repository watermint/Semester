package stenographer.views

import com.twitter.finatra.{Request, Response, Controller}
import java.util.UUID
import org.jboss.netty.handler.codec.http.{Cookie, DefaultCookie}
import etude.commons.utility.html.Csrf

class Admin extends Controller {
  val adminUserCookieName = "U"

  get("/admin") {
    request =>
      ???
  }

  put("/admin/chat_index/:roomId") {
    request =>
      ???
  }

  delete("/admin/chat_index/:roomId") {
    request =>
      ???
  }

  def notFound(): Response = {
    render
      .plain("")
      .status(404)
  }

  def redirect(uri: String): Response = {
    render
      .plain("")
      .status(302)
      .header("Location", uri)
  }

  def csrfAndCookie(request: Request): Pair[Csrf, Option[Cookie]] = {
    request.cookies.getValue(adminUserCookieName) match {
      case Some(c) => Csrf(c) -> None
      case _ => {
        val uuid = UUID.randomUUID().toString
        val adminUserCookie = new DefaultCookie(adminUserCookieName, uuid)
        Csrf(uuid) -> Some(adminUserCookie)
      }
    }
  }
}
