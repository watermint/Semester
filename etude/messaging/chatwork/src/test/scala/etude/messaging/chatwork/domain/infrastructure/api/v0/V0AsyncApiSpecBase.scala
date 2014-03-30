package etude.messaging.chatwork.domain.infrastructure.api.v0

import java.util.Properties
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.ExecutionContext
import org.specs2.execute.Result
import etude.test.undisclosed._
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api

trait V0AsyncApiSpecBase {
  def getEntityIOContext(prop: Properties): AsyncEntityIOContextOnV0Api = {
    (prop.getProperty("organizationId"),
      prop.getProperty("email"),
      prop.getProperty("password")) match {

      case (_, null, _) => throw new IllegalStateException(s"Property 'email' required for test")
      case (_, _, null) => throw new IllegalStateException(s"Property 'password' required for test")
      case (null, email, password) => AsyncEntityIOContextOnV0Api(email, password)
      case ("", email, password) => AsyncEntityIOContextOnV0Api(email, password)
      case (orgId, email, password) => AsyncEntityIOContextOnV0Api(orgId, email, password)
    }
  }

  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  def withContext(spec: AsyncEntityIOContextOnV0Api => Result): Result = {
    undisclosed("etude.chatwork.infrastructure.api.v0.V0AsyncApi") {
      properties =>
        spec(getEntityIOContext(properties))
    }
  }
}
