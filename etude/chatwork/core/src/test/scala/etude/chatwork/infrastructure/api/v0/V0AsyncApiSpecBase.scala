package etude.chatwork.infrastructure.api.v0

import java.util.Properties
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.ExecutionContext
import org.specs2.execute.Result
import etude.test.undisclosed._

trait V0AsyncApiSpecBase {
  def getEntityIOContext(prop: Properties): V0AsyncEntityIOContext = {
    (prop.getProperty("organizationId"),
      prop.getProperty("email"),
      prop.getProperty("password")) match {

      case (_, null, _) => throw new IllegalStateException(s"Property 'email' required for test")
      case (_, _, null) => throw new IllegalStateException(s"Property 'password' required for test")
      case (null, email, password) => V0AsyncEntityIOContext(email, password)
      case ("", email, password) => V0AsyncEntityIOContext(email, password)
      case (orgId, email, password) => V0AsyncEntityIOContext(orgId, email, password)
    }
  }

  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  def withContext(spec: V0AsyncEntityIOContext => Result): Result = {
    undisclosed("etude.chatwork.infrastructure.api.v0.V0AsyncApi") {
      properties =>
        spec(getEntityIOContext(properties))
    }
  }
}
