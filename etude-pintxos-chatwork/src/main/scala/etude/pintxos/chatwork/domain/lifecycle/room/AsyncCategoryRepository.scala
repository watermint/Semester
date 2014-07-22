package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.manieres.domain.lifecycle.async.AsyncRepository
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.model.room.{Category, CategoryId}

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