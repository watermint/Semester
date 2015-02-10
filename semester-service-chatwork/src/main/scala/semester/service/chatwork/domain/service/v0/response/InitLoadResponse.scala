package semester.service.chatwork.domain.service.v0.response

import semester.service.chatwork.domain.model.account.Account
import semester.service.chatwork.domain.model.room.{Participant, Room}
import semester.service.chatwork.domain.service.v0.request.InitLoadRequest
import org.json4s.JValue

case class InitLoadResponse(rawResponse: JValue,
                            request: InitLoadRequest,
                            contacts: List[Account],
                            rooms: List[Room],
                            participants: List[Participant],
                            lastId: Option[String])
  extends ChatWorkResponse {
  type Request = InitLoadRequest
}
