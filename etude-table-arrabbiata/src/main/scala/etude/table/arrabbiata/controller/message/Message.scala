package etude.table.arrabbiata.controller.message

import etude.table.arrabbiata.controller.AppActor

trait Message {
  implicit val executionContext = AppActor.executionContext
}
