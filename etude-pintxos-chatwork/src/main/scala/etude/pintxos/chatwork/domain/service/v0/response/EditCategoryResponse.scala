package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.room.Category
import etude.pintxos.chatwork.domain.service.v0.request.EditCategoryRequest
import org.json4s.JValue

case class EditCategoryResponse(rawResponse: JValue,
                                request: EditCategoryRequest,
                                category: Category)
  extends ChatWorkResponse[EditCategoryRequest]

