package etude.messaging.chatwork.domain.infrastructure.api.v1

import scala.concurrent.{ExecutionContext, Await, Future}
import scala.concurrent.duration._
import java.util.Properties
import java.util.concurrent.{Executors, ExecutorService}
import org.specs2.execute.Result
import etude.test.undisclosed._
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV1Api

trait V1AsyncApiSpecBase {
  def result[T](f: Future[T]): T = {
    Await.result(f, Duration(30, SECONDS))
  }

  def getEntityIOContext(prop: Properties): AsyncEntityIOContextOnV1Api = {
    AsyncEntityIOContextOnV1Api(prop.getProperty("token"))
  }

  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  def withContext(spec: AsyncEntityIOContextOnV1Api => Result): Result = {
    undisclosed("etude.chatwork.infrastructure.api.v1.V1AsyncApi") {
      properties =>
        spec(getEntityIOContext(properties))
    }
  }
}
