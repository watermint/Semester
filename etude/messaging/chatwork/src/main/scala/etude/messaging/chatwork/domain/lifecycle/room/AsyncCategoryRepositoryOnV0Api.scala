package etude.messaging.chatwork.domain.lifecycle.room

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.{ResultWithEntity, EntityIOContext}
import etude.messaging.chatwork.domain.model.room.{RoomId, CategoryId, Category}
import etude.messaging.chatwork.domain.infrastructure.api.v0.V0AsyncApi
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import etude.foundation.domain.lifecycle.async.AsyncResultWithEntity
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOOnV0Api

private[room]
class AsyncCategoryRepositoryOnV0Api
  extends AsyncCategoryRepository
  with AsyncEntityIOOnV0Api {

  type This <: AsyncCategoryRepositoryOnV0Api

  def parseCategory(json: JValue): List[Category] = {
    for {
      JObject(catDat) <- json
      JField(categoryId, JObject(category)) <- catDat
      JField("name", JString(name)) <- category
      JField("list", JArray(list)) <- category
    } yield {
      val rooms = for {
        JString(room) <- list
      } yield {
        RoomId(BigInt(room))
      }

      new Category(
        categoryId = CategoryId(BigInt(categoryId)),
        name = name,
        rooms = rooms
      )
    }
  }

  def categories()(implicit context: EntityIOContext[Future]): Future[List[Category]] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncApi.api(
      "get_category",
      Map()
    ) map {
      json =>
        parseCategory(json)
    }
  }

  def containsByIdentity(identity: CategoryId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    categories() map {
      c =>
        c.exists(_.categoryId.equals(identity))
    }
  }

  def resolve(identity: CategoryId)(implicit context: EntityIOContext[Future]): Future[Category] = {
    implicit val executor = getExecutionContext(context)
    categories() map {
      c =>
        c.find(_.categoryId.equals(identity)).get
    }
  }

  def create(name: String, rooms: List[RoomId])(implicit context: EntityIOContext[Future]): Future[ResultWithEntity[This, CategoryId, Category, Future]] = {
    implicit val executor = getExecutionContext(context)

    val pdata = ("name" -> name) ~
      ("r" -> rooms.map(_.value.toString()))

    V0AsyncApi.api(
      "add_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        AsyncResultWithEntity(this.asInstanceOf[This], parseCategory(json).last)
    }
  }

  def deleteByIdentity(identity: CategoryId)(implicit context: EntityIOContext[Future]): Future[ResultWithEntity[This, CategoryId, Category, Future]] = {
    implicit val executor = getExecutionContext(context)

    resolve(identity) flatMap {
      e =>
        val pdata = "cat_id" -> identity.value.toString()

        V0AsyncApi.api(
          "delete_category",
          Map(),
          Map(
            "pdata" -> compact(render(pdata))
          )
        ) map {
          json =>
            AsyncResultWithEntity(this.asInstanceOf[This], e)
        }
    }
  }

  def store(entity: Category)(implicit context: EntityIOContext[Future]): Future[ResultWithEntity[This, CategoryId, Category, Future]] = {
    implicit val executor = getExecutionContext(context)

    val pdata = ("name" -> entity.name) ~
      ("r" -> entity.rooms.map(_.value.toString())) ~
      ("cat_id" -> entity.categoryId.value.toString())

    V0AsyncApi.api(
      "edit_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        AsyncResultWithEntity(this.asInstanceOf[This], entity)
    }
  }
}
