package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.room.Category
import etude.pintxos.chatwork.domain.service.v0.request.{AddCategoryRequest, ChatWorkRequest}
import org.json4s.JValue

case class AddCategoryResponse(rawResponse: JValue,
                               request: AddCategoryRequest,
                               category: Category)
  extends ChatWorkResponse[AddCategoryRequest]
