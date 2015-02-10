package semester.service.chatwork.domain.service.v0.command

import semester.service.chatwork.domain.service.v0.request.DeleteCategoryRequest
import semester.service.chatwork.domain.service.v0.response.DeleteCategoryResponse
import semester.service.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object DeleteCategory
  extends ChatWorkCommand[DeleteCategoryRequest, DeleteCategoryResponse] {


  def execute(request: DeleteCategoryRequest)(implicit context: ChatWorkIOContext): DeleteCategoryResponse = {
    val pdata = "cat_id" -> request.categoryId.value.toString()
    val json = ChatWorkApi.api(
      "delete_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    )

    DeleteCategoryResponse(
      json,
      request,
      request.categoryId
    )
  }
}
