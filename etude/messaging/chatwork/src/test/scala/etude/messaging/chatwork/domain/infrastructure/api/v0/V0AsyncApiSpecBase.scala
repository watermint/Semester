package etude.messaging.chatwork.domain.infrastructure.api.v0

import java.util.Properties
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.ExecutionContext
import org.specs2.execute.Result
import etude.test.undisclosed._
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.domain.core.lifecycle.async.AsyncEntityIOContext

trait V0AsyncApiSpecBase {
  def getEntityIOContext(prop: Properties)(implicit executionContext: ExecutionContext): AsyncEntityIOContext = {
    (prop.getProperty("organizationId"),
      prop.getProperty("email"),
      prop.getProperty("password")) match {

      case (_, null, _) => throw new IllegalStateException(s"Property 'email' required for test")
      case (_, _, null) => throw new IllegalStateException(s"Property 'password' required for test")
      case (null, username, password) => AsyncEntityIOContextOnV0Api(username, password)
      case ("", username, password) => AsyncEntityIOContextOnV0Api(username, password)
      case (orgId, username, password) => AsyncEntityIOContextOnV0Api(orgId, username, password)
    }
  }

  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  def withContext(spec: AsyncEntityIOContext => Result): Result = {
    undisclosed("etude.chatwork.infrastructure.api.v0.V0AsyncApi") {
      properties =>
        spec(getEntityIOContext(properties))
    }
  }
}
