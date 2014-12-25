package etude.pintxos.chatwork.domain.infrastructure.api.v0.response

import etude.pintxos.chatwork.domain.model.room.Category
import org.json4s.JValue

case class EditCategoryResponse(rawResponse: JValue,
                                 category: Category)
  extends ChatWorkResponse

