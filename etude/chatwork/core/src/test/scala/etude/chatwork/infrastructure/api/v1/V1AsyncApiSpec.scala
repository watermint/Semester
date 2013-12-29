package etude.chatwork.infrastructure.api.v1

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration._
import etude.test.undisclosed._
import java.util.Properties
import etude.chatwork.domain.room.Room

@RunWith(classOf[JUnitRunner])
class V1AsyncApiSpec extends Specification {
  def result[T](f: Future[T]): T = {
    Await.result(f, Duration(30, SECONDS))
  }

  def getEntityIOContext(prop: Properties): V1AsyncEntityIOContext = {
    V1AsyncEntityIOContext(prop.getProperty("token"))
  }

  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  "Undisclosed (secret API token for chatwork account required) test" should {
    "RoomRepository" in {
      undisclosed(getClass.getName) {
        properties =>
          implicit val context = getEntityIOContext(properties)
          val roomRepo = new V1RoomRepository()
          val firstRoom: Future[Room] = roomRepo.rooms() map {
            rooms =>
              rooms(0)
          }
          val room: Room = result(firstRoom)

          room.identity.value.toInt must beGreaterThan(0)

          result(roomRepo.resolve(room.identity)) must equalTo(room)
      }
    }
  }
}
