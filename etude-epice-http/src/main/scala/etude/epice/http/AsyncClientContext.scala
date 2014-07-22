package etude.epice.http

import scala.concurrent.ExecutionContext

case class AsyncClientContext(executionContext: ExecutionContext) extends ClientContext
