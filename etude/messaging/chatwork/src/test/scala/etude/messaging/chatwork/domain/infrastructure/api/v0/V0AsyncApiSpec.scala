package etude.messaging.chatwork.domain.infrastructure.api.v0

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api


@RunWith(classOf[JUnitRunner])
class V0AsyncApiSpec
  extends Specification
  with V0AsyncApiSpecBase {

  "Undisclosed (email/password for chatwork account required) test" should {
    "Login" in {
      withContext {
        implicit context =>
          V0AsyncApi.login.isSuccess must beTrue

          context match {
            case c: AsyncEntityIOContextOnV0Api =>
              c.myId.isDefined must beTrue
              c.accessToken.isDefined must beTrue
          }
      }
    }

    "init_load" in {
      withContext {
        implicit context =>
          V0AsyncApi.syncApi("init_load", Map())
          context match {
            case c: AsyncEntityIOContextOnV0Api =>
              c.myId.isDefined must beTrue
              c.accessToken.isDefined must beTrue
          }
      }
    }
  }
}
