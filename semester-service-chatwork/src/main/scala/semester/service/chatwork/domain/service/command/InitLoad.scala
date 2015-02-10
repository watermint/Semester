package semester.service.chatwork.domain.service.command

import org.json4s._
import semester.foundation.logging.LoggerFactory
import semester.service.chatwork.domain.service.parser.{ContactParser, ParticipantParser, RoomParser}
import semester.service.chatwork.domain.service.request.InitLoadRequest
import semester.service.chatwork.domain.service.response.InitLoadResponse
import semester.service.chatwork.domain.service.{ChatWorkApi, ChatWorkIOContext}

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
