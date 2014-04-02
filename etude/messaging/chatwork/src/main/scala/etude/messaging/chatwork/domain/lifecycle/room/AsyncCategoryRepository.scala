package etude.messaging.chatwork.domain.lifecycle.room

import etude.foundation.domain.lifecycle.async.AsyncRepository
import etude.messaging.chatwork.domain.model.room.{CategoryId, Category}
import scala.concurrent.Future

trait AsyncCategoryRepository
  extends AsyncRepository[CategoryId, Category]
  with CategoryRepository[Future] {

  type This <: AsyncCategoryRepository

}

object AsyncCategoryRepository {
  def ofV0Api(): AsyncCategoryRepository =
    new AsyncCategoryRepositoryOnV0Api()
}