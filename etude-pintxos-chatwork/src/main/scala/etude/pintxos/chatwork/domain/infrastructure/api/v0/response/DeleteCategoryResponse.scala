package etude.pintxos.chatwork.domain.infrastructure.api.v0.response

import etude.pintxos.chatwork.domain.model.room.CategoryId
import org.json4s.JValue

case class DeleteCategoryResponse(rawResponse: JValue,
                                  categoryId: CategoryId)
  extends ChatWorkResponse
