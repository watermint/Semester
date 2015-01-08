package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.Api
import etude.pintxos.chatwork.domain.service.v0.parser.CategoryParser
import etude.pintxos.chatwork.domain.service.v0.request.GetCategoryRequest
import etude.pintxos.chatwork.domain.service.v0.response.GetCategoryResponse

import scala.concurrent.Future

object GetCategory
  extends ChatWorkCommand[GetCategoryRequest, GetCategoryResponse] {

  def execute(request: GetCategoryRequest)(implicit context: EntityIOContext[Future]): GetCategoryResponse = {
    implicit val executor = getExecutionContext(context)
    val json = Api.api(
      "get_category",
      Map()
    )

    GetCategoryResponse(
      json,
      CategoryParser.parseCategory(json)
    )
  }
}
