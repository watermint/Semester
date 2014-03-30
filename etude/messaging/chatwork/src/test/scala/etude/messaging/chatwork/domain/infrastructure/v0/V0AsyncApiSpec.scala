package etude.messaging.chatwork.domain.infrastructure.v0

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification


@RunWith(classOf[JUnitRunner])
class V0AsyncApiSpec
  extends Specification
  with V0AsyncApiSpecBase {

  "Undisclosed (email/password for chatwork account required) test" should {
    "Login" in {
      withContext {
        implicit context =>
          V0AsyncApi.login.isSuccess must beTrue
          context.myId.isDefined must beTrue
          context.accessToken.isDefined must beTrue
      }
    }

    "init_load" in {
      withContext {
        implicit context =>
          V0AsyncApi.syncApi("init_load", Map())
          context.myId.isDefined must beTrue
          context.accessToken.isDefined must beTrue
      }
    }
  }
}
