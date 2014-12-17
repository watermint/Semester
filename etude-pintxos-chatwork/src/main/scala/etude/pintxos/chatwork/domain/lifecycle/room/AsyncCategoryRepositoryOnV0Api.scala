package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.async.AsyncResultWithIdentity
import etude.manieres.domain.lifecycle.{EntityIOContext, ResultWithIdentity}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncEntityIO
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.{AddCategory, DeleteCategory, EditCategory, GetCategory}
import etude.pintxos.chatwork.domain.model.room.{Category, CategoryId, RoomId}

import scala.concurrent.Future

private[room]
class AsyncCategoryRepositoryOnV0Api
  extends AsyncCategoryRepository
  with V0AsyncEntityIO {

  type This <: AsyncCategoryRepositoryOnV0Api

  def containsByIdentity(identity: CategoryId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    categories() map {
      c =>
        c.exists(_.categoryId.equals(identity))
    }
  }

  def categories()(implicit context: EntityIOContext[Future]): Future[List[Category]] = {
    GetCategory.categories()
  }

  def resolve(identity: CategoryId)(implicit context: EntityIOContext[Future]): Future[Category] = {
    implicit val executor = getExecutionContext(context)
    categories() map {
      c =>
        c.find(_.categoryId.equals(identity)).get
    }
  }

  def create(name: String, rooms: List[RoomId])(implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, CategoryId, Category, Future]] = {
    implicit val executor = getExecutionContext(context)

    AddCategory.create(name, rooms) map {
      category =>
        AsyncResultWithIdentity(this.asInstanceOf[This], category.identity)
    }
  }

  def deleteByIdentity(identity: CategoryId)(implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, CategoryId, Category, Future]] = {
    implicit val executor = getExecutionContext(context)

    resolve(identity) flatMap {
      e =>
        DeleteCategory.delete(identity) map {
          j =>
            AsyncResultWithIdentity(this.asInstanceOf[This], identity)
        }
    }
  }

  def store(entity: Category)(implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, CategoryId, Category, Future]] = {
    implicit val executor = getExecutionContext(context)

    EditCategory.edit(entity) map {
      c =>
        AsyncResultWithIdentity(this.asInstanceOf[This], entity.identity)
    }
  }
}
