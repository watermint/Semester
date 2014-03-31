package etude.messaging.chatwork.domain.lifecycle.message

import etude.messaging.chatwork.domain.model.room.Room
import etude.messaging.chatwork.domain.lifecycle.room.AsyncRoomRepository
import etude.messaging.chatwork.domain.model.message.Text
import etude.messaging.chatwork.domain.infrastructure.api.v1.V1AsyncApiSpecBase
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import etude.test.undisclosed._

@RunWith(classOf[JUnitRunner])
class AsyncMessageRepositoryOnV1ApiSpec
  extends Specification
  with V1AsyncApiSpecBase {

  "Undisclosed (secret API token for chatwork account required) test" should {
    "Message write/read" in {
      undisclosed(getClass.getName) {
        properties =>
          implicit val context = getEntityIOContext(properties)
          val roomRepo = AsyncRoomRepository.ofV1Api()
          val messageRepo = AsyncMessageRepository.ofV1Api()

          val myRoom: Room = result(roomRepo.myRoom())

          val text = Text(s"Test: ${getClass.getName} ${java.util.UUID.randomUUID()}")
          val message = messageRepo.say(text)(myRoom)
          val testMessage = result(message).get

          testMessage.messageId.toInt must beGreaterThan(0)

          val resolvedTestMessage = result(messageRepo.resolve(testMessage))

          resolvedTestMessage.body must equalTo(text.text)
      }
    }
  }
}