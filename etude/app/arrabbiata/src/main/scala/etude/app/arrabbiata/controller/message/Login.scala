package etude.app.arrabbiata.controller.message

case class Login(username: String, password: String, orgId: String) extends WithoutSession {
  def perform(): Unit = ???
}
