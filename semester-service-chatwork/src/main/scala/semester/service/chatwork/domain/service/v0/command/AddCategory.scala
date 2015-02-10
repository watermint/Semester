package semester.service.chatwork.domain.service.v0.command

import semester.service.chatwork.domain.service.v0.parser.CategoryParser
import semester.service.chatwork.domain.service.v0.request.AddCategoryRequest
import semester.service.chatwork.domain.service.v0.response.AddCategoryResponse
import semester.service.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object AddCategory
  extends ChatWorkCommand[AddCategoryRequest, AddCategoryResponse] {

  def execute(request: AddCategoryRequest)(implicit context: ChatWorkIOContext): AddCategoryResponse = {
    val pdata = ("name" -> request.name) ~
      ("r" -> request.rooms.map(_.value.toString()))

    val json = ChatWorkApi.api(
      "add_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    )

    AddCategoryResponse(
      json,
      request,
      CategoryParser.parseCategory(json).last
    )
  }

}