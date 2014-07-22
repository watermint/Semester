package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.async.AsyncResultWithIdentity
import etude.manieres.domain.lifecycle.{EntityIOContext, ResultWithIdentity}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.room.{Category, CategoryId, RoomId}
import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

private[room]
class AsyncCategoryRepositoryOnV0Api
  extends AsyncCategoryRepository
  with V0AsyncEntityIO {

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

  def create(name: String, rooms: List[RoomId])(implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, CategoryId, Category, Future]] = {
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
        AsyncResultWithIdentity(this.asInstanceOf[This], parseCategory(json).last.identity)
    }
  }

  def deleteByIdentity(identity: CategoryId)(implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, CategoryId, Category, Future]] = {
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
            AsyncResultWithIdentity(this.asInstanceOf[This], identity)
        }
    }
  }

  def store(entity: Category)(implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, CategoryId, Category, Future]] = {
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
        AsyncResultWithIdentity(this.asInstanceOf[This], entity.identity)
    }
  }
}
