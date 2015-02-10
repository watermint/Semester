package semester.service.chatwork.domain.service.v0.command

import semester.service.chatwork.domain.service.v0.parser.CategoryParser
import semester.service.chatwork.domain.service.v0.request.GetCategoryRequest
import semester.service.chatwork.domain.service.v0.response.GetCategoryResponse
import semester.service.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}

object GetCategory
  extends ChatWorkCommand[GetCategoryRequest, GetCategoryResponse] {

  def execute(request: GetCategoryRequest)(implicit context: ChatWorkIOContext): GetCategoryResponse = {
    val json = ChatWorkApi.api(
      "get_category",
      Map()
    )

    GetCategoryResponse(
      json,
      request,
      CategoryParser.parseCategory(json)
    )
  }
}
