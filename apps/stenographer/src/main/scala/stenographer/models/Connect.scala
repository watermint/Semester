package stenographer.models

import com.twitter.finagle.http.Cookie
import scala.collection.mutable
import etude.chatwork.v0.Session

object Connect {
  private var session: Option[Session] = None

  private val adminUserIds = mutable.ListBuffer[String]()

  def currentSession: Option[Session] = session

  def unlessAdminUser(userIdCookie: Option[Cookie]): Boolean = !isAdminUser(userIdCookie)

  def isAdminUser(userIdCookie: Option[Cookie]): Boolean = {
    userIdCookie match {
      case Some(c) => isAdminUser(c.value)
      case _ => false
    }
  }

  def isAdminUser(userId: String): Boolean = adminUserIds.contains(userId)

  def logout(): Unit = {
    session = None
    adminUserIds.clear()
  }

  def login(adminUid: String, email: String, password: String, orgId: Option[String]): Either[Exception, Session] = {
    currentSession match {
      case Some(cs) =>
        if (!cs.email.equals(email)) {
          return Left(InvalidLoginException("This Stenographer instance already configured for other ChatWork account."))
        }
      case _ =>
    }

    val s = Session(email, password, orgId)
    val r = s.login

    r match {
      case Left(e) => Left(e)
      case Right(r) => {
        session = Some(s)
        adminUserIds.append(adminUid)
        Right(s)
      }
    }
  }
}
