package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.CategoryParser
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.room.{Category, RoomId}
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import scala.concurrent.Future

object AddCategory
  extends V0AsyncEntityIO {

  def create(name: String, rooms: List[RoomId])(implicit context: EntityIOContext[Future]): Future[Category] = {
    implicit val executor = getExecutionContext(context)

    val pdata = ("name" -> name) ~
      ("r" -> rooms.map(_.value.toString()))

    V0AsyncApi.api(
      "add_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        CategoryParser.parseCategory(json).last
    }

  }
}