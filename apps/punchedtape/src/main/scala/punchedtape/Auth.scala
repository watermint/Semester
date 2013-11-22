package punchedtape

import etude.chatwork.Session

case class Auth(orgId: Option[String],
                email: String,
                password: String) {
  lazy val session: Session = Session(
    email = email,
    password = password,
    orgId = orgId
  )
}