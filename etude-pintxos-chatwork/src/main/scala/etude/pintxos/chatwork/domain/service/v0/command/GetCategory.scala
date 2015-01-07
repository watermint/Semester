package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.V0AsyncApi
import etude.pintxos.chatwork.domain.service.v0.parser.CategoryParser
import etude.pintxos.chatwork.domain.service.v0.request.GetCategoryRequest
import etude.pintxos.chatwork.domain.service.v0.response.GetCategoryResponse

import scala.concurrent.Future

object GetCategory
  extends ChatWorkCommand[GetCategoryRequest, GetCategoryResponse] {

  def execute(request: GetCategoryRequest)(implicit context: EntityIOContext[Future]): Future[GetCategoryResponse] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncApi.api(
      "get_category",
      Map()
    ) map {
      json =>
        GetCategoryResponse(
          json,
          CategoryParser.parseCategory(json)
        )
    }
  }
}
