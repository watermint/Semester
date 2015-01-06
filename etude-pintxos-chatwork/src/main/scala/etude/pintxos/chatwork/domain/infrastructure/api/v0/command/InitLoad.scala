package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import java.time.Instant

import etude.epice.logging.LoggerFactory
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.{ContactParser, ParticipantParser, RoomParser}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.InitLoadRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.InitLoadResponse
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import org.json4s._

import scala.collection.mutable
import scala.concurrent.Future

/**
 * facade for jumbo api 'init_load'.
 */
object InitLoad
  extends ChatWorkCommand[InitLoadRequest, InitLoadResponse] {

  val logger = LoggerFactory.getLogger(getClass)

  def parseLastId(json: JValue): Option[String] = {
    val lastId: List[String] = for {
      JObject(doc) <- json
      JField("last_id", JString(lastId)) <- doc
    } yield {
      lastId
    }
    lastId.lastOption
  }


  def execute(request: InitLoadRequest)(implicit context: EntityIOContext[Future]): Future[InitLoadResponse] = {
    implicit val executor = getExecutionContext(context)

    V0AsyncApi.api("init_load", Map()) map {
      json =>
        parseLastId(json) match {
          case Some(lastId) => setLastId(lastId, context)
          case _ =>
        }

        val content = InitLoadResponse(
          json,
          contacts = ContactParser.parseContacts(json),
          rooms = RoomParser.parseRooms(json),
          participants = ParticipantParser.parseParticipants(json)
        )

        content
    }
  }
}
