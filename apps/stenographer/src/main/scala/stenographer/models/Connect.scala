package stenographer.models

import etude.chatwork.Session

object Connect {
  private var session: Option[Session] = None

  def currentSession: Option[Session] = session

  def logout(): Unit = {
    session = None
  }

  def login(email: String, password: String, orgId: Option[String]): Either[Exception, Session] = {
    session match {
      case Some(s) => Right(s)
      case _ => {
        val s = Session(email, password, orgId)
        val r = s.login

        r match {
          case Left(e) => Left(e)
          case Right(r) => {
            session = Some(s)
            Right(s)
          }
        }
      }
    }
  }
}
