package semester.service.chatwork.domain.service.command

import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import semester.service.chatwork.domain.service.parser.CategoryParser
import semester.service.chatwork.domain.service.request.AddCategoryRequest
import semester.service.chatwork.domain.service.response.AddCategoryResponse
import semester.service.chatwork.domain.service.{ChatWorkApi, ChatWorkIOContext}

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