package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.room.Category
import etude.pintxos.chatwork.domain.service.v0.request.GetCategoryRequest
import org.json4s.JValue

case class GetCategoryResponse(rawResponse: JValue,
                               request: GetCategoryRequest,
                               categories: Seq[Category])
  extends ChatWorkResponse[GetCategoryRequest]
