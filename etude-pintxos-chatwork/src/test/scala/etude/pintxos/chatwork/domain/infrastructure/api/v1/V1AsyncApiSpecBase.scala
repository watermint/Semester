package etude.pintxos.chatwork.domain.infrastructure.api.v1

import java.util.Properties
import java.util.concurrent.{ExecutorService, Executors}

import etude.manieres.domain.lifecycle.async.AsyncEntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV1Api
import etude.epice.undisclosed._
import org.specs2.execute.Result

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

trait V1AsyncApiSpecBase {
  def result[T](f: Future[T]): T = {
    Await.result(f, Duration(30, SECONDS))
  }

  def getEntityIOContext(prop: Properties): AsyncEntityIOContext = {
    AsyncEntityIOContextOnV1Api(prop.getProperty("token"))
  }

  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  def withContext(spec: AsyncEntityIOContext => Result): Result = {
    undisclosed("etude.chatwork.infrastructure.api.v1.V1AsyncApi") {
      properties =>
        spec(getEntityIOContext(properties))
    }
  }
}
