package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.pintxos.chatwork.domain.model.room.CategoryId

case class DeleteCategoryRequest(categoryId: CategoryId)
  extends ChatWorkRequest
