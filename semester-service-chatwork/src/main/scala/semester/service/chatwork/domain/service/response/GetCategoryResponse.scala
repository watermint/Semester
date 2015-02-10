package semester.service.chatwork.domain.service.response

import semester.service.chatwork.domain.model.room.Category
import semester.service.chatwork.domain.service.request.GetCategoryRequest
import org.json4s.JValue

case class GetCategoryResponse(rawResponse: JValue,
                               request: GetCategoryRequest,
                               categories: Seq[Category])
  extends ChatWorkResponse {
  type Request = GetCategoryRequest
}
