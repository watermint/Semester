package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.room.Category
import org.json4s.JValue

case class AddCategoryResponse(rawResponse: JValue,
                               category: Category)
  extends ChatWorkResponse