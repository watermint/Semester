package etude.messaging.chatwork.domain.lifecycle.room

import etude.domain.core.lifecycle.EntityIOContext
import etude.domain.core.lifecycle.async.AsyncRepository
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.messaging.chatwork.domain.model.room.{Category, CategoryId}

import scala.concurrent.Future

trait AsyncCategoryRepository
  extends AsyncRepository[CategoryId, Category]
  with CategoryRepository[Future] {

  type This <: AsyncCategoryRepository

}

object AsyncCategoryRepository {
  def ofContext(context: EntityIOContext[Future]): AsyncCategoryRepository = {
    context match {
      case c: AsyncEntityIOContextOnV0Api => ofV0Api()
      case _ => throw new IllegalArgumentException("Unsupported EntityIOContext")
    }
  }

  private def ofV0Api(): AsyncCategoryRepository =
    new AsyncCategoryRepositoryOnV0Api()
}