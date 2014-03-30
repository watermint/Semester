package etude.messaging.chatwork.domain.infrastructure.v1

import scala.concurrent.{ExecutionContext, Await, Future}
import scala.concurrent.duration._
import java.util.Properties
import java.util.concurrent.{Executors, ExecutorService}
import org.specs2.execute.Result
import etude.test.undisclosed._

trait V1AsyncApiSpecBase {
  def result[T](f: Future[T]): T = {
    Await.result(f, Duration(30, SECONDS))
  }

  def getEntityIOContext(prop: Properties): V1AsyncEntityIOContext = {
    V1AsyncEntityIOContext(prop.getProperty("token"))
  }

  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  def withContext(spec: V1AsyncEntityIOContext => Result): Result = {
    undisclosed("etude.chatwork.infrastructure.api.v1.V1AsyncApi") {
      properties =>
        spec(getEntityIOContext(properties))
    }
  }
}
