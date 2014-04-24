package etude.domain.core.lifecycle.async

import etude.domain.core.lifecycle.{EntityIOContext, EntityIO}
import scala.concurrent.{ExecutionContext, Future}

trait AsyncEntityIO extends EntityIO {
  protected def getExecutionContext(context: EntityIOContext[Future]): ExecutionContext = {
    context match {
      case ac: AsyncEntityIOContext => ac.executor
      case _ => throw new IllegalArgumentException(s"$context must compatible with AsyncEntityIOContext")
    }
  }
}