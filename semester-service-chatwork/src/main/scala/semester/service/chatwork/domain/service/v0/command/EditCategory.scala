package semester.service.chatwork.domain.service.v0.command

import semester.service.chatwork.domain.service.v0.request.EditCategoryRequest
import semester.service.chatwork.domain.service.v0.response.EditCategoryResponse
import semester.service.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object EditCategory
  extends ChatWorkCommand[EditCategoryRequest, EditCategoryResponse] {

  def execute(request: EditCategoryRequest)(implicit context: ChatWorkIOContext): EditCategoryResponse = {
    val pdata = ("name" -> request.category.name) ~
      ("r" -> request.category.rooms.map(_.value.toString())) ~
      ("cat_id" -> request.category.categoryId.value.toString())
    val json = ChatWorkApi.api(
      "edit_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    )

    // TODO : parse json
    EditCategoryResponse(
      json,
      request,
      request.category
    )
  }
}
