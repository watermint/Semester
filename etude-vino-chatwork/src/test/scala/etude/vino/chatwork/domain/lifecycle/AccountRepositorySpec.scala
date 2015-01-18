package etude.vino.chatwork.domain.lifecycle

import java.net.URI

import etude.pintxos.chatwork.domain.model.account.{Account, AccountId}
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import org.elasticsearch.index.query.QueryBuilders
import org.junit.runner.RunWith
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AccountRepositorySpec extends Specification {
  val engine = ElasticSearch("unit-test")
  val accountRepo = AccountRepository(engine)
  val account1 = new Account(
    AccountId(12345),
    Some("Blue"),
    None,
    None,
    None,
    Some(new URI("http://example.com/12345.jpg"))
  )
  val account2 = new Account(
    AccountId(56789),
    Some("Red"),
    None,
    None,
    None,
    Some(new URI("http://example.com/56789.jpg"))
  )
  val account3 = new Account(
    AccountId(23456),
    Some("Red"),
    None,
    None,
    None,
    Some(new URI("http://example.com/23456.jpg"))
  )

  "AccountRepository" should {
    "JSON serialize/deserialize" in {
      val json = accountRepo.toJson(account1)
      val a = accountRepo.fromJsonSeq(None, json).last

      account1 must equalTo(a)
    }

    "Delete/Update/Get" in {
      def deleteUpdateGet(account: Account): MatchResult[_] = {
        accountRepo.delete(account)
        accountRepo.update(account) >= 0 must beTrue

        val a = accountRepo.get(account.accountId)

        a must beSome[Account]
        a.get must equalTo(account)
      }

      try {
        deleteUpdateGet(account1)
        deleteUpdateGet(account2)
        deleteUpdateGet(account3)
      } finally {
        engine.flushAndRefresh()
      }
    }

    "Search : exact match" in {
      def exactMatch(account: Account): MatchResult[_] = {
        val accountByGet = accountRepo.get(account.accountId)
        accountByGet must beSome[Account]
        accountByGet.get must equalTo(account)

        val result = accountRepo.search(QueryBuilders.matchQuery("accountId", accountRepo.toIdentity(account.accountId)))
        result.size must equalTo(1)
        result must contain(account)
      }

      exactMatch(account1)
      exactMatch(account2)
      exactMatch(account3)
    }

    "Search : term" in {
      val result = accountRepo.search(QueryBuilders.termQuery("name", "Red"))
      result.size must equalTo(2)
      result must contain(account2)
      result must contain(account3)
    }
  }

  engine.client.close()
}
