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

  def flush: Future[Boolean] = {
    future {
      engine
        .client
        .admin()
        .indices()
        .prepareFlush(indexName)
        .setFull(true)
        .execute()
        .get()
        .getFailedShards < 1
    }
  }

  def indexType(typeName: String): Type = {
    Type(indexName, typeName)
  }

}
