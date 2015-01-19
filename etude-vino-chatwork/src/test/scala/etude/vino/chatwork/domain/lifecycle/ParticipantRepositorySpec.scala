package etude.vino.chatwork.domain.lifecycle

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.room.{RoomId, Participant}
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import org.junit.runner.RunWith
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ParticipantRepositorySpec
  extends Specification {
  val engine = ElasticSearch(testMode = true)
  val repository = ParticipantRepository(engine)
  val participant1 = new Participant(
    roomId = RoomId(1234),
    admin = Seq(AccountId(1000), AccountId(1001)),
    member = Seq(),
    readonly = Seq()
  )
  val participant2 = new Participant(
    roomId = RoomId(2345),
    admin = Seq(AccountId(1000)),
    member = Seq(AccountId(1001)),
    readonly = Seq(AccountId(2001))
  )
  val participant3 = new Participant(
    roomId = RoomId(3456),
    admin = Seq(AccountId(1000)),
    member = Seq(AccountId(1001)),
    readonly = Seq(AccountId(2001), AccountId(2002))
  )

  "ParticipantRepository" should {
    "JSON serialize/deserialize" in {
      def serializeDeserialize(participant: Participant): MatchResult[_] = {
        val json = repository.toJson(participant)
        val p = repository.fromJsonSeq(None, json).last
        p must equalTo(participant)
      }

      serializeDeserialize(participant1)
      serializeDeserialize(participant2)
      serializeDeserialize(participant3)
    }

    "Delete/Update/Get" in {
      def deleteUpdateGet(participant: Participant): MatchResult[_] = {
        repository.delete(participant)
        repository.update(participant) >= 0 must beTrue

        val p = repository.get(participant.identity)

        p must beSome
        p.get must equalTo(participant)
      }

      deleteUpdateGet(participant1)
      deleteUpdateGet(participant2)
      deleteUpdateGet(participant3)
    }
  }

  engine.shutdown()
}
