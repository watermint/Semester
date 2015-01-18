package etude.vino.chatwork.domain.lifecycle

import java.time.Instant

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.{Text, MessageId, Message}
import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import org.elasticsearch.index.query.{FilterBuilders, QueryBuilders}
import org.junit.runner.RunWith
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MessageRepositorySpec
  extends Specification {

  val engine = ElasticSearch(testMode = true)
  val messageRepo = MessageRepository(engine)
  val message1 = new Message(
    MessageId(RoomId(123), 1001),
    AccountId(123001),
    Text("オリーブオイルと生ハム"),
    Instant.parse("2014-01-01T00:00:00Z"),
    None
  )
  val message2 = new Message(
    MessageId(RoomId(123), 1002),
    AccountId(234002),
    Text("バルサミコ酢とフォカッチャ"),
    Instant.parse("2015-01-01T00:00:00Z"),
    None
  )
  val message3 = new Message(
    MessageId(RoomId(123), 1002),
    AccountId(234002),
    Text("オリーブとローストビーフ"),
    Instant.parse("2014-06-01T00:00:00Z"),
    None
  )

  "MessageRepository" should {
    "JSON serialize/deserialize" in {
      def serializeDeserialize(message: Message): MatchResult[_] = {
        val json = messageRepo.toJson(message)
        val m = messageRepo.fromJsonSeq(Some(messageRepo.toIdentity(message.messageId)), json).last
        m must equalTo(message)
      }
      serializeDeserialize(message1)
      serializeDeserialize(message2)
      serializeDeserialize(message3)
    }

    "Delete/Update/Get/Search" in {
      def deleteUpdateGet(message: Message): MatchResult[_] = {
        messageRepo.delete(message)
        messageRepo.update(message) >= 0 must beTrue

        engine.flushAndRefresh()

        val m = messageRepo.get(message.messageId)
        m must beSome
        m.get must equalTo(message)
      }

      deleteUpdateGet(message1)
      deleteUpdateGet(message2)
      deleteUpdateGet(message3)

      def searchTerm(): MatchResult[_] = {
        val result = messageRepo.search(QueryBuilders.termQuery("account", "234002"))
        result.size must equalTo(2)
        result must contain(message2)
        result must contain(message3)
      }
      def searchTermWithDateRange(): MatchResult[_] = {
        val result = messageRepo.search(
          QueryBuilders.boolQuery()
            .must(QueryBuilders.termQuery("account", "234002"))
            .must(QueryBuilders.rangeQuery("@timestamp").from("2014-05-01T00:00:00Z").to("2014-07-01T00:00:00Z")))

        result.size must equalTo(1)
        result must contain(message2)
      }
      def searchJapaneseTerm(): MatchResult[_] = {
        val result = messageRepo.search(QueryBuilders.termQuery("body", "オリーブ"))
        result.size must equalTo(2)
        result must contain(message1)
        result must contain(message3)
      }

      searchTerm()
      searchTermWithDateRange()
      searchJapaneseTerm()
    }
  }

  engine.shutdown()
}
