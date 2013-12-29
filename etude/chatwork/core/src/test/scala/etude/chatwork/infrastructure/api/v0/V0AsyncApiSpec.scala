package etude.chatwork.infrastructure.api.v0

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import etude.test.undisclosed._
import java.util.Properties
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.ExecutionContext


@RunWith(classOf[JUnitRunner])
class V0AsyncApiSpec extends Specification {
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

  "Undisclosed (email/password for chatwork account required) test" should {
    "Login" in {
      undisclosed(getClass.getName) {
        properties =>
          implicit val context = getEntityIOContext(properties)
          V0AsyncApi.login.isSuccess must beTrue
          context.myId.isDefined must beTrue
          context.accessToken.isDefined must beTrue
      }
    }
    
    "init_load" in {
      undisclosed(getClass.getName) {
        properties =>
          implicit val context = getEntityIOContext(properties)
          V0AsyncApi.syncApi("init_load", Map())
          context.myId.isDefined must beTrue
          context.accessToken.isDefined must beTrue
      }
    }
  }
}
