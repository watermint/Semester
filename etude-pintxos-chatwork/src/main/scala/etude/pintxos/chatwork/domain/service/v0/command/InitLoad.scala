package etude.pintxos.chatwork.domain.service.v0.command

import etude.epice.logging.LoggerFactory
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.Api
import etude.pintxos.chatwork.domain.service.v0.parser.{ContactParser, ParticipantParser, RoomParser}
import etude.pintxos.chatwork.domain.service.v0.request.InitLoadRequest
import etude.pintxos.chatwork.domain.service.v0.response.InitLoadResponse
import org.json4s._

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


  def execute(request: InitLoadRequest)(implicit context: EntityIOContext[Future]): InitLoadResponse = {
    implicit val executor = getExecutionContext(context)

    val json = Api.api("init_load", Map())

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
