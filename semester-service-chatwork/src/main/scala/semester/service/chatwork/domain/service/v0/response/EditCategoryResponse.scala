package semester.service.chatwork.domain.service.v0.response

import semester.service.chatwork.domain.model.room.Category
import semester.service.chatwork.domain.service.v0.request.EditCategoryRequest
import org.json4s.JValue

case class EditCategoryResponse(rawResponse: JValue,
                                request: EditCategoryRequest,
                                category: Category)
  extends ChatWorkResponse {
  type Request = EditCategoryRequest
}

