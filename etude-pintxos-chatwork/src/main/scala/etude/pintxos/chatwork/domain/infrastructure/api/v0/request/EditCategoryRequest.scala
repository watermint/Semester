package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.pintxos.chatwork.domain.model.room.Category

case class EditCategoryRequest(category: Category)
  extends ChatWorkRequest

