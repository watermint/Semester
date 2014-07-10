package etude.domain.elasticsearch

import etude.domain.core.lifecycle.async.{AsyncRepository, AsyncResultWithIdentity}
import etude.domain.core.lifecycle.{EntityIOContext, ResultWithIdentity}
import etude.domain.core.model.{Entity, Identity}
import etude.kitchenette.elasticsearch.Engine

import scala.concurrent.Future

trait AsyncRepositoryOnElasticSearch[ID <: Identity[_], E <: Entity[ID]] extends AsyncRepository[ID, E] {
  val engine: Engine

  def indexValue(identity: ID): String

  def indexValues(): Seq[String]

  val typeValue: String

  def idValue(identity: ID): String

  def marshal(entity: E): String

  def unmarshal(json: String): E

  def prepare()(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executionContext = getExecutionContext(context)

    Future {
      indexValues().foreach {
        index =>
          engine
            .client
            .admin()
            .indices()
            .prepareCreate(index)
            .setIndex(index)
            .execute()
      }
      true
    }
  }

  def store(entity: E)
           (implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, ID, E, Future]] = {
    implicit val executionContext = getExecutionContext(context)

    Future {
      engine
        .client
        .prepareIndex()
        .setIndex(indexValue(entity.identity))
        .setId(idValue(entity.identity))
        .setType(typeValue)
        .setSource(marshal(entity))
        .execute()
        .get()

      AsyncResultWithIdentity(this.asInstanceOf[This], entity.identity)
    }
  }

  def deleteByIdentity(identity: ID)
                      (implicit context: EntityIOContext[Future]): Future[ResultWithIdentity[This, ID, E, Future]] = {
    implicit val executionContext = getExecutionContext(context)

    Future {
      engine
        .client
        .prepareDelete()
        .setIndex(indexValue(identity))
        .setType(typeValue)
        .setId(idValue(identity))
        .execute()
        .get()

      AsyncResultWithIdentity(this.asInstanceOf[This], identity)
    }
  }

  def containsByIdentity(identity: ID)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executionContext = getExecutionContext(context)

    Future {
      val response = engine
        .client
        .prepareGet()
        .setIndex(indexValue(identity))
        .setType(typeValue)
        .setId(idValue(identity))
        .execute()
        .get()

      response.isExists
    }
  }

  def resolve(identity: ID)(implicit context: EntityIOContext[Future]): Future[E] = {
    implicit val executionContext = getExecutionContext(context)

    Future {
      val response = engine
        .client
        .prepareGet()
        .setIndex(indexValue(identity))
        .setType(typeValue)
        .setId(idValue(identity))
        .execute()
        .get()

      if (response.isExists) {
        unmarshal(response.getSourceAsString)
      } else {
        throw new IllegalArgumentException(s"$identity not found")
      }
    }
  }
}
