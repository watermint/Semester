package etude.pintxos.chatwork.domain.service.v0

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner


@RunWith(classOf[JUnitRunner])
class V0AsyncApiSpec
  extends Specification
  with V0AsyncApiSpecBase {

  "Undisclosed (email/password for chatwork account required) test" should {
    "Login" in {
      withContext {
        implicit context =>
          ChatWorkApi.login.isSuccess must beTrue

          context match {
            case c: ChatWorkIOContext =>
              c.myId.isSet must beTrue
              c.accessToken.isSet must beTrue
          }
      }
    }

//    "init_load" in {
//      withContext {
//        implicit context =>
//          V0AsyncApi.syncApi("init_load", Map())
//          context match {
//            case c: AsyncEntityIOContextOnV0Api =>
//              c.myId.isSet must beTrue
//              c.accessToken.isSet must beTrue
//          }
//      }
//    }
  }
}
