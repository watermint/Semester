package semester.service.chatwork.domain.model.room

import semester.foundation.domain.model.Entity

class Category(val categoryId: CategoryId,
               val name: String,
               val rooms: List[RoomId]) extends Entity[CategoryId] {
  val identity: CategoryId = categoryId

  def copy(name: String = this.name,
           rooms: List[RoomId] = this.rooms): Category = {

    new Category(
      categoryId = categoryId,
      name = name,
      rooms = rooms
    )
  }
}
