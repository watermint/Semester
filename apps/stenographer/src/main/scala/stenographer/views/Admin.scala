package stenographer.views

import com.twitter.finatra.{Request, Response, Controller}
import java.util.UUID
import org.jboss.netty.handler.codec.http.{Cookie, DefaultCookie}
import stenographer.models.{Storage, Indexer, Connect}
import etude.http.security.Csrf
import stenographer.views.admin.{Main, Login}
import stenographer.models.Indexer.{IndexerStatusNotInitialized, IndexerStatusLoading}

class Admin extends Controller {
  val adminUserCookieName = "U"

  get("/admin") {
    request =>
      top(request).toFuture
  }

  put("/admin/chat_index/:roomId") {
    request =>
      ???
  }

  delete("/admin/chat_index/:roomId") {
    request =>
      ???
  }

  post("/admin/chat_detail/:roomId") {
    request =>
      if (Connect.isAdminUser(request.cookies.get(adminUserCookieName))) {
        val roomId = request.routeParams.get("roomId")
        render.html(<div class="container">{roomId}</div>.toString).toFuture
      } else {
        notFound().toFuture
      }
  }

  post("/admin/chat_list.json") {
    request =>
      if (Connect.unlessAdminUser(request.cookies.get(adminUserCookieName))) {
        notFound().toFuture
      } else {
        val query = request.params.get("q")
        val rooms = (query match {
          case Some(q) =>
            Storage.loadedRooms filter (_._2.description.getOrElse("").toLowerCase.contains(q.toLowerCase))
          case _ =>
            Storage.loadedRooms
        }).values.toList.sortBy(_.description)

        render.json(
          Map(
            "loading" -> (Indexer.currentStatus match {
              case _: IndexerStatusLoading => true
              case _: IndexerStatusNotInitialized => true
              case _ => false
            }),
            "list" -> (rooms map (
              r =>
                Map(
                  "id" -> r.roomId.toString,
                  "indexed" -> true,
                  "title" -> r.description.getOrElse("")
                )
              )
              )
          )
        ).toFuture
      }
  }

  post("/admin/auth") {
    request => {
      val csrf = csrfAndCookie(request)._1
      val token = request.params.get("csrfToken")
      val orgId = request.params.get("orgId")
      val email = request.params.get("email")
      val password = request.params.get("password")

      val response = (request.cookies.get(adminUserCookieName), token) match {
        case (Some(c), Some(t)) => {
          Csrf(c.value).verify(t) match {
            case false => top(request, Some("Session expired. Please enter email and password."))
            case true =>
              (email, password, orgId) match {
                case (Some(e), Some(p), o) => auth(csrf, e, p, o)
                case _ => render.html(Login.loginMenu(csrf, Some("Please enter email and/or password.")))
              }
          }
        }
        case _ => redirect("/admin")
      }
      response.toFuture
    }
  }

  def top(request: Request, errorForLogin: Option[String] = None): Response = {
    val token = csrfAndCookie(request)

    val response = (Connect.isAdminUser(request.cookies.get(adminUserCookieName)), Connect.currentSession) match {
      case (true, Some(s)) => render.html(Main.adminMenu())
      case _ => render.html(Login.loginMenu(token._1, errorForLogin))
    }

    token._2 foreach (c => response.cookie(c))

    response
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

  def auth(csrf: Csrf, email: String, password: String, orgId: Option[String]): Response = {
    Connect.login(csrf.userSeed, email, password, orgId) match {
      case Right(s) => {
        Indexer.ref ! Indexer.InitialLoad()
        redirect("/admin")
      }
      case Left(x) =>
        render.html(Login.loginMenu(csrf, Some("Login failed with message: " + x.getMessage)))
    }
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
