package etude.app.arrabbiata.controller.message

import etude.app.arrabbiata.state.Session

trait MessageWithSession extends Message {
  def perform(session: Session): Unit
}
