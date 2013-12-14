package etude.elasticsearch

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

case class Index(indexName: String)(implicit engine: Engine) {
  def exists: Future[Boolean] = {
    future {
      engine
        .client
        .admin()
        .indices()
        .prepareExists(indexName)
        .execute()
        .get()
        .isExists
    }
  }

  def create: Future[Boolean] = {
    future {
      engine
        .client
        .admin()
        .indices()
        .prepareCreate(indexName)
        .execute()
        .get()
        .isAcknowledged
    }
  }

  def delete: Future[Boolean] = {
    future {
      engine
        .client
        .admin()
        .indices()
        .prepareDelete(indexName)
        .execute()
        .get()
        .isAcknowledged
    }
  }

  def indexType(typeName: String): Type = {
    Type(indexName, typeName)
  }

}
