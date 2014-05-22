package etude.app.arrabbiata.controller.message

import etude.app.arrabbiata.controller.Session

trait WithSession extends Message {
  val session: Session
}
