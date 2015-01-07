package etude.pintxos.chatwork.domain.service.v0.parser

import etude.pintxos.chatwork.domain.model.room.{CategoryId, RoomId, Category}
import org.json4s.JsonAST.{JArray, JString, JField, JObject}
import org.json4s._

object CategoryParser {

  def parseCategory(json: JValue): List[Category] = {
    for {
      JObject(catDat) <- json
      JField(categoryId, JObject(category)) <- catDat
      JField("name", JString(name)) <- category
      JField("list", JArray(list)) <- category
    } yield {
      val rooms = for {
        JString(room) <- list
      } yield {
        RoomId(BigInt(room))
      }

      new Category(
        categoryId = CategoryId(BigInt(categoryId)),
        name = name,
        rooms = rooms
      )
    }
  }

}
