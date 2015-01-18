package etude.vino.chatwork.domain.lifecycle

import java.net.URI

import etude.pintxos.chatwork.domain.model.account.{Account, AccountId}
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AccountRepositorySpec extends Specification {
  val engine = ElasticSearch("unit-test")
  val accountRepo = AccountRepository(engine)
  val account = new Account(
    AccountId(12345),
    Some("test 12345"),
    None,
    None,
    None,
    Some(new URI("http://example.com/12345.jpg"))
  )

  "AccountRepository" should {
    "JSON serialize/deserialize" in {
      val json = accountRepo.toJson(account)
      val a = accountRepo.fromJsonSeq("", json).last

      account must equalTo(a)
    }
  }

  engine.client.close()
}
