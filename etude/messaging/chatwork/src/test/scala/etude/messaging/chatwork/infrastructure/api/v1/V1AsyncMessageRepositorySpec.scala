package etude.messaging.chatwork.infrastructure.api.v1

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import etude.test.undisclosed._
import etude.messaging.chatwork.domain.room.Room
import etude.messaging.chatwork.domain.message.Text

@RunWith(classOf[JUnitRunner])
class V1AsyncMessageRepositorySpec
  extends Specification
  with V1AsyncApiSpecBase {

  "Undisclosed (secret API token for chatwork account required) test" should {
    "Message write/read" in {
      undisclosed(getClass.getName) {
        properties =>
          implicit val context = getEntityIOContext(properties)
          val roomRepo = new V1AsyncRoomRepository()
          val messageFactory = new V1AsyncMessageFactory()
          val messageRepo = new V1AsyncMessageRepository()

          val myRoom: Room = result(roomRepo.myRoom())

          val text = Text(s"Test: ${getClass.getName} ${java.util.UUID.randomUUID()}")
          val message = messageFactory.create(text)(myRoom)
          val testMessage = result(message)

          testMessage.messageId.toInt must beGreaterThan(0)

          val resolvedTestMessage = result(messageRepo.resolve(testMessage))

          resolvedTestMessage.body must equalTo(text.text)
      }
    }
  }
}