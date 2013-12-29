package etude.chatwork.infrastructure.api.v1

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import scala.concurrent.Future
import etude.test.undisclosed._
import etude.chatwork.domain.room.{RoomType, RoomId, Room}
import etude.chatwork.domain.message.Text

@RunWith(classOf[JUnitRunner])
class V1AsyncRoomRepositorySpec
  extends Specification
  with V1AsyncApiSpecBase {

  "Undisclosed (secret API token for chatwork account required) test" should {
    "rooms" in {
      withContext {
        implicit context =>
          val roomRepo = new V1RoomRepository()
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
          val roomRepo = new V1RoomRepository()
          val myRoom: Room = result(roomRepo.myRoom())

          RoomType.isMyRoom(myRoom.roomType) must beTrue
          myRoom.identity.value.toInt must beGreaterThan(0)
      }
    }
  }

}
