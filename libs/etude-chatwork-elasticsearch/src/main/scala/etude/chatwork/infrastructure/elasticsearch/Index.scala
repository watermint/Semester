package etude.chatwork.infrastructure.elasticsearch

import scala.concurrent._
import org.json4s._
import org.json4s.native.JsonMethods._

case class Index(name: String)(implicit engine: Engine) {
  def exists: Future[Boolean] = {
    future {
      engine
        .client
        .admin()
        .indices()
        .prepareExists(name)
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
        .prepareCreate(name)
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
        .prepareDelete(name)
        .execute()
        .get()
        .isAcknowledged
    }
  }

  def putMapping(typeName: String, mapping: JValue): Future[Boolean] = {
    future {
      engine
        .client
        .admin()
        .indices()
        .preparePutMapping(name)
        .setType(typeName)
        .setSource(render(mapping))
        .execute()
        .get()
        .isAcknowledged
    }
  }
}
