package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.room.CategoryId
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

object DeleteCategory
  extends V0AsyncEntityIO {

  def delete(categoryId: CategoryId)(implicit context: EntityIOContext[Future]): Future[CategoryId] = {
    implicit val executor = getExecutionContext(context)
    val pdata = "cat_id" -> categoryId.value.toString()

    V0AsyncApi.api(
      "delete_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        // TODO: parse json
        categoryId
    }

  }
}
