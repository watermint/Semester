package etude.app.arrabbiata.controller.message

import etude.app.arrabbiata.controller.AppActor

trait Message {
  implicit val executionContext = AppActor.executionContext
}
