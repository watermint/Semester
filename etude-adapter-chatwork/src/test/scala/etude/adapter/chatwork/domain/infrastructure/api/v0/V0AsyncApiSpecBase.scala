package etude.adapter.chatwork.domain.infrastructure.api.v0

import java.util.Properties
import java.util.concurrent.{ExecutorService, Executors}

import etude.domain.core.lifecycle.async.AsyncEntityIOContext
import etude.adapter.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.test.undisclosed._
import org.specs2.execute.Result

import scala.concurrent.ExecutionContext

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
