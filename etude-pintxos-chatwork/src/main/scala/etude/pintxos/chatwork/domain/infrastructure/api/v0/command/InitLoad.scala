package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import java.time.Instant

import etude.epice.logging.LoggerFactory
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.{ContactParser, ParticipantParser, RoomParser}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.InitLoadResponse
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import org.json4s._

import scala.collection.mutable
import scala.concurrent.Future

/**
 * facade for jumbo api 'init_load'.
 */
object InitLoad
  extends V0AsyncEntityIO {

  val logger = LoggerFactory.getLogger(getClass)

  case class InitLoadContainer(content: InitLoadResponse,
                               loadTime: Instant)

  private val cache = new mutable.HashMap[String, InitLoadContainer]()

  private val cacheSeconds = 300

  def parseLastId(json: JValue): Option[String] = {
    val lastId: List[String] = for {
      JObject(doc) <- json
      JField("last_id", JString(lastId)) <- doc
    } yield {
      lastId
    }
    lastId.lastOption
  }


  def initLoad()(implicit context: EntityIOContext[Future]): Future[InitLoadResponse] = {
    implicit val executor = getExecutionContext(context)

    getMyId(context) match {
      case None => // nop
      case Some(myId) =>
        cache.get(myId) match {
          case None => // nop
          case Some(cached) =>
            if (Instant.now.minusSeconds(cacheSeconds).isBefore(cached.loadTime)) {
              logger.debug("init load from cache")
              return Future.successful(cached.content)
            }
        }
    }

    V0AsyncApi.api("init_load", Map()) map {
      json =>
        parseLastId(json) match {
          case Some(lastId) => setLastId(lastId, context)
          case _ =>
        }
        val content = InitLoadResponse(
          contacts = ContactParser.parseContacts(json),
          rooms = RoomParser.parseRooms(json),
          participants = ParticipantParser.parseParticipants(json)
        )

        cache.put(
          getMyId(context).get,
          InitLoadContainer(
            content,
            Instant.now()
          )
        )
        logger.debug("init load finished")

        content
    }
  }
}
