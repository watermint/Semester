package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.request.DeleteCategoryRequest
import etude.pintxos.chatwork.domain.service.v0.response.DeleteCategoryResponse
import etude.pintxos.chatwork.domain.service.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.room.CategoryId
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

object DeleteCategory
  extends ChatWorkCommand[DeleteCategoryRequest, DeleteCategoryResponse] {


  def execute(request: DeleteCategoryRequest)(implicit context: EntityIOContext[Future]): Future[DeleteCategoryResponse] = {
    implicit val executor = getExecutionContext(context)
    val pdata = "cat_id" -> request.categoryId.value.toString()

    V0AsyncApi.api(
      "delete_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        DeleteCategoryResponse(
          json,
          request.categoryId
        )
    }
  }
}
