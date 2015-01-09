package etude.pintxos.chatwork.domain.service.v0.command

import etude.pintxos.chatwork.domain.service.v0.parser.CategoryParser
import etude.pintxos.chatwork.domain.service.v0.request.GetCategoryRequest
import etude.pintxos.chatwork.domain.service.v0.response.GetCategoryResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}

object GetCategory
  extends ChatWorkCommand[GetCategoryRequest, GetCategoryResponse] {

  def execute(request: GetCategoryRequest)(implicit context: ChatWorkIOContext): GetCategoryResponse = {
    val json = ChatWorkApi.api(
      "get_category",
      Map()
    )

    GetCategoryResponse(
      json,
      CategoryParser.parseCategory(json)
    )
  }
}
