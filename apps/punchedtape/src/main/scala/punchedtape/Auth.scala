package punchedtape

import etude.chatwork.v0.Session

case class Auth(orgId: Option[String],
                email: String,
                password: String) {
  lazy val session: Session = Session(
    email = email,
    password = password,
    orgId = orgId
  )
}