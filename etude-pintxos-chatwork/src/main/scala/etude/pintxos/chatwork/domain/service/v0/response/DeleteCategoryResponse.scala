package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.room.CategoryId
import etude.pintxos.chatwork.domain.service.v0.request.DeleteCategoryRequest
import org.json4s.JValue

case class DeleteCategoryResponse(rawResponse: JValue,
                                  request: DeleteCategoryRequest,
                                  categoryId: CategoryId)
  extends ChatWorkResponse {
  type Request = DeleteCategoryRequest
}
