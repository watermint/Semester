package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.AddCategory._
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.room.Category
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

object EditCategory
  extends V0AsyncEntityIO {

  def edit(category: Category)(implicit context: EntityIOContext[Future]): Future[Category] = {
    implicit val executor = getExecutionContext(context)
    val pdata = ("name" -> category.name) ~
      ("r" -> category.rooms.map(_.value.toString())) ~
      ("cat_id" -> category.categoryId.value.toString())

    V0AsyncApi.api(
      "edit_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        // TODO : parse json
        category
    }

  }
}
