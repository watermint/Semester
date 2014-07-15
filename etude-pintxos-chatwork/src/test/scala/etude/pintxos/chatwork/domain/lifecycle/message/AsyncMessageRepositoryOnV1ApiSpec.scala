package etude.pintxos.chatwork.domain.lifecycle.message

import etude.pintxos.chatwork.domain.infrastructure.api.v1.V1AsyncApiSpecBase
import etude.pintxos.chatwork.domain.lifecycle.room.AsyncRoomRepository
import etude.pintxos.chatwork.domain.model.message.Text
import etude.pintxos.chatwork.domain.model.room.Room
import etude.test.undisclosed._
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AsyncMessageRepositoryOnV1ApiSpec
  extends Specification
  with V1AsyncApiSpecBase {

  "Undisclosed (secret API token for chatwork account required) test" should {
    "Message write/read" in {
      undisclosed(getClass.getName) {
        properties =>
          implicit val context = getEntityIOContext(properties)
          val roomRepo = AsyncRoomRepository.ofContext(context)
          val messageRepo = AsyncMessageRepository.ofContext(context)

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