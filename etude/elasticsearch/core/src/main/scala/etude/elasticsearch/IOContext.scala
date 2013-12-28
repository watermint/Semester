package etude.elasticsearch

import scala.concurrent.ExecutionContext

trait IOContext {
  val engine: Engine

  val executor: ExecutionContext
}

object IOContext {
  def apply(engine: Engine)(implicit executor: ExecutionContext): IOContext = {
    IOContextImpl(engine, executor)
  }
}

private[elasticsearch]
case class IOContextImpl(engine: Engine,
                         executor: ExecutionContext)
  extends IOContext