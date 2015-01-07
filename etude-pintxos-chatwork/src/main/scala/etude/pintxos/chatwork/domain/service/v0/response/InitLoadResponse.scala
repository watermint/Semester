package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.room.{Participant, Room}
import org.json4s.JValue

case class InitLoadResponse(rawResponse: JValue,
                            contacts: List[Account],
                            rooms: List[Room],
                            participants: List[Participant])
  extends ChatWorkResponse
