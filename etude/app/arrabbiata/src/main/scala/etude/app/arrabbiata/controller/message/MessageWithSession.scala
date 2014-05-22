package etude.app.arrabbiata.controller.message

import etude.app.arrabbiata.controller.Session

trait MessageWithSession extends Message {
  def perform(session: Session): Unit
}
