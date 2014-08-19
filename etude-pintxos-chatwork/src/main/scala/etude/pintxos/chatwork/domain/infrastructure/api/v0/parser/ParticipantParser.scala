package etude.pintxos.chatwork.domain.infrastructure.api.v0.parser

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.room.{RoomId, Participant}
import org.json4s.JsonAST.{JInt, JField, JObject}
import org.json4s._

object ParticipantParser extends ParserBase {

  def parseParticipants(json: JValue): List[Participant] = {
    for {
      JObject(doc) <- json
      JField("room_dat", JObject(roomDat)) <- doc
      JField(roomId, JObject(room)) <- roomDat
      JField("m", JObject(member)) <- room
    } yield {
      val accounts: Seq[(AccountId, Int)] = member.map {
        p =>
          AccountId(BigInt(p._1)) -> (p._2 match {
            case JInt(role) => role.toInt
            case _ => 0
          })
      }

      new Participant(
        roomId = RoomId(BigInt(roomId)),
        admin = accounts.filter(_._2 == 1).map(_._1),
        member = accounts.filter(_._2 == 2).map(_._1),
        readonly = accounts.filter(_._2 == 3).map(_._1)
      )
    }
  }

}