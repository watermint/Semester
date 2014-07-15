package etude.table.arrabbiata.controller.message

import etude.table.arrabbiata.state.Session

trait MessageWithSession extends Message {
  def perform(session: Session): Unit
}
