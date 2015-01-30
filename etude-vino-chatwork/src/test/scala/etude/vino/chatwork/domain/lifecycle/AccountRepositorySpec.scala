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
  val engine = ElasticSearch(testMode = true)
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
      def serializeDeserialize(account: Account): MatchResult[_] = {
        val json = accountRepo.toJson(account)
        val a = accountRepo.fromJsonSeq(None, json).last
        a must equalTo(account)
      }

      serializeDeserialize(account1)
      serializeDeserialize(account2)
      serializeDeserialize(account3)
    }

    "Delete/Update/Get/Search" in {
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

      def exactMatch(account: Account): MatchResult[_] = {
        val accountByGet = accountRepo.get(account.accountId)
        accountByGet must beSome[Account]
        accountByGet.get must equalTo(account)

        val result = accountRepo.search(QueryBuilders.matchQuery("accountId", accountRepo.toIdentity(account.accountId)), None)
        result.entities.size must equalTo(1)
        result.entities must contain(account)
      }

      exactMatch(account1)
      exactMatch(account2)
      exactMatch(account3)

      def searchTerm(): MatchResult[_] = {
        val result = accountRepo.search(QueryBuilders.termQuery("name", "Red"), None)
        result.entities.size must equalTo(2)
        result.entities must contain(account2)
        result.entities must contain(account3)
      }

      searchTerm()
    }
  }

  engine.shutdown()
}
