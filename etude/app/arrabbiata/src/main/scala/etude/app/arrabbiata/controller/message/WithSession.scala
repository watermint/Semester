package etude.app.arrabbiata.controller.message

import etude.app.arrabbiata.controller.Session

trait WithSession extends Action {
  val session: Session
}
