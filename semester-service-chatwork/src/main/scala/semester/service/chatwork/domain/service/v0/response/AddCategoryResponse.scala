package semester.service.chatwork.domain.service.v0.response

import semester.service.chatwork.domain.model.room.Category
import semester.service.chatwork.domain.service.v0.request.AddCategoryRequest
import org.json4s.JValue

case class AddCategoryResponse(rawResponse: JValue,
                               request: AddCategoryRequest,
                               category: Category)
  extends ChatWorkResponse {
  type Request = AddCategoryRequest
}
