package etude.vino.chatwork.domain.lifecycle

import java.time.Instant

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.{Text, MessageId, Message}
import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
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
    Text("message text by 123001"),
    Instant.parse("2014-01-01T00:00:00Z"),
    None
  )
  val message2 = new Message(
    MessageId(RoomId(123), 1002),
    AccountId(234002),
    Text("message text bu 234002"),
    Instant.parse("2015-01-01T00:00:00Z"),
    None
  )
  val message3 = new Message(
    MessageId(RoomId(123), 1002),
    AccountId(234002),
    Text("message text bu 234002"),
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
  }

  engine.shutdown()
}
