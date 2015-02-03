package etude.vino.chatwork.domain.lifecycle

import java.net.URI

import etude.pintxos.chatwork.domain.model.room.{RoomTypeGroup, RoomId, Room}
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import org.junit.runner.RunWith
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class RoomRepositorySpec
  extends Specification {
  val engine = ElasticSearch(testMode = true, httpEnabled = false)
  val repository = RoomRepository(engine)
  val room1 = new Room(
    roomId = RoomId(123),
    name = "Room 123",
    description = None,
    attributes = None,
    roomType = RoomTypeGroup(),
    avatar = Some(new URI("http://example.com/123.jpg")),
    lastUpdateTime = None
  )
  val room2 = new Room(
    roomId = RoomId(234),
    name = "Room 234",
    description = None,
    attributes = None,
    roomType = RoomTypeGroup(),
    avatar = Some(new URI("http://example.com/234.jpg")),
    lastUpdateTime = None
  )
  val room3 = new Room(
    roomId = RoomId(345),
    name = "Room 345",
    description = None,
    attributes = None,
    roomType = RoomTypeGroup(),
    avatar = Some(new URI("http://example.com/345.jpg")),
    lastUpdateTime = None
  )

  "RoomRepository" should {
    "JSON serialize/deserialize" in {
      def serializeDeserialize(room: Room): MatchResult[_] = {
        val json = repository.toJson(room)
        val r = repository.fromJsonSeq(None, json).last
        r must equalTo(room)
      }

      serializeDeserialize(room1)
      serializeDeserialize(room2)
      serializeDeserialize(room3)
    }

    "Delete/Update/Get" in {
      def deleteUpdateGet(room: Room): MatchResult[_] = {
        repository.delete(room)
        repository.update(room) >= 0 must beTrue

        val r = repository.get(room.identity)

        r must beSome
        r.get must equalTo(room)
      }

      deleteUpdateGet(room1)
      deleteUpdateGet(room2)
      deleteUpdateGet(room3)
    }
  }

  engine.shutdown()
}
