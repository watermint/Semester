package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.Api
import etude.pintxos.chatwork.domain.service.v0.request.EditCategoryRequest
import etude.pintxos.chatwork.domain.service.v0.response.EditCategoryResponse
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

object EditCategory
  extends ChatWorkCommand[EditCategoryRequest, EditCategoryResponse] {

  def execute(request: EditCategoryRequest)(implicit context: EntityIOContext[Future]): EditCategoryResponse = {
    implicit val executor = getExecutionContext(context)
    val pdata = ("name" -> request.category.name) ~
      ("r" -> request.category.rooms.map(_.value.toString())) ~
      ("cat_id" -> request.category.categoryId.value.toString())
    val json = Api.api(
      "edit_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    )

    // TODO : parse json
    EditCategoryResponse(
      json,
      request.category
    )
  }
}
