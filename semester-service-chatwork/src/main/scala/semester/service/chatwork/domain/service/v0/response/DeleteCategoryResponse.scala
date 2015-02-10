package semester.service.chatwork.domain.service.v0.response

import semester.service.chatwork.domain.model.room.CategoryId
import semester.service.chatwork.domain.service.v0.request.DeleteCategoryRequest
import org.json4s.JValue

case class DeleteCategoryResponse(rawResponse: JValue,
                                  request: DeleteCategoryRequest,
                                  categoryId: CategoryId)
  extends ChatWorkResponse {
  type Request = DeleteCategoryRequest
}
