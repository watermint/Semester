package etude.pintxos.chatwork.domain.lifecycle.room

import etude.pintxos.chatwork.domain.infrastructure.api.v1.V1AsyncApiSpecBase
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId, RoomType}
import etude.epice.undisclosed._
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class AsyncRoomRepositoryOnV1ApiSpec
  extends Specification
  with V1AsyncApiSpecBase {

  "Undisclosed (secret API token for chatwork account required) test" should {
    "rooms" in {
      withContext {
        implicit context =>
          val roomRepo = new AsyncRoomRepositoryOnV1Api()
          val firstRoom: Future[Room] = roomRepo.rooms() map {
            rooms =>
              rooms(0)
          }
          val room: Room = result(firstRoom)

          room.identity.value.toInt must beGreaterThan(0)

          result(roomRepo.resolve(room.identity)) must equalTo(room)
          result(roomRepo.containsByIdentity(room.identity)) must beTrue
          result(roomRepo.containsByIdentity(RoomId(12345))) must beFalse
      }
    }

    "myRoom" in {
      undisclosed(getClass.getName) {
        properties =>
          implicit val context = getEntityIOContext(properties)
          val roomRepo = new AsyncRoomRepositoryOnV1Api()
          val myRoom: Room = result(roomRepo.myRoom())

          RoomType.isMyRoom(myRoom.roomType) must beTrue
          myRoom.identity.value.toInt must beGreaterThan(0)
      }
    }
  }

}
