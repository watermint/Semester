package etude.pintxos.chatwork.domain.service.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.command.AddCategory
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.pintxos.chatwork.domain.model.room.RoomId

import scala.concurrent.Future

case class AddCategoryRequest(name: String,
                              rooms: List[RoomId])
  extends ChatWorkRequest {

  def execute(implicit context: EntityIOContext[Future]): ChatWorkResponse = {
    AddCategory.execute(this)
  }
}
