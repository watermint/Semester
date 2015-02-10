package semester.service.chatwork.domain.service.command

import semester.service.chatwork.domain.service.parser.CategoryParser
import semester.service.chatwork.domain.service.request.GetCategoryRequest
import semester.service.chatwork.domain.service.response.GetCategoryResponse
import semester.service.chatwork.domain.service.{ChatWorkApi, ChatWorkIOContext}

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
