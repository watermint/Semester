package etude.chatwork.infrastructure.api.v0

import scala.concurrent.{ExecutionContext, Future}
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext

trait V0AsyncEntityIOContext
  extends V0EntityIOContext[Future]
  with AsyncEntityIOContext

private[v0]
class V0AsyncEntityIOContextImpl(val organizationId: Option[String],
                                 val email: String,
                                 val password: String)
                                (implicit val executor: ExecutionContext)
  extends V0AsyncEntityIOContext
