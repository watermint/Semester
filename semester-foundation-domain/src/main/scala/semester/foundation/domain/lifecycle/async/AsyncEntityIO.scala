package semester.foundation.domain.lifecycle.async

import semester.foundation.domain.lifecycle.{EntityIO, EntityIOContext}

import scala.concurrent.{ExecutionContext, Future}

trait AsyncEntityIO extends EntityIO {
  protected def getExecutionContext(context: EntityIOContext[Future]): ExecutionContext = {
    context match {
      case ac: AsyncEntityIOContext => ac.executor
      case _ => throw new IllegalArgumentException(s"$context must compatible with AsyncEntityIOContext")
    }
  }
}