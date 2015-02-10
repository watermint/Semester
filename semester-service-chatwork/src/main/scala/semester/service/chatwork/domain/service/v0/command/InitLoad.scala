package semester.service.chatwork.domain.service.v0.command

import semester.foundation.logging.LoggerFactory
import semester.service.chatwork.domain.service.v0.parser.{ContactParser, ParticipantParser, RoomParser}
import semester.service.chatwork.domain.service.v0.request.InitLoadRequest
import semester.service.chatwork.domain.service.v0.response.InitLoadResponse
import semester.service.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import org.json4s._

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


  def execute(request: InitLoadRequest)(implicit context: ChatWorkIOContext): InitLoadResponse = {

    val json = ChatWorkApi.api("init_load", Map())
    val lastId = parseLastId(json)

    lastId.foreach(setLastId(_, context))

    val content = InitLoadResponse(
      json,
      request,
      contacts = ContactParser.parseContacts(json),
      rooms = RoomParser.parseRooms(json),
      participants = ParticipantParser.parseParticipants(json),
      lastId = lastId
    )

    content
  }
}
