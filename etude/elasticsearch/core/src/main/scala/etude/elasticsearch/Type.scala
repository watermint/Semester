package etude.elasticsearch

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.json4s.JValue
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.index.query.{QueryBuilder, QueryBuilders}
import org.elasticsearch.action.get.GetResponse

case class Type(indexName: String, typeName: String)(implicit engine: Engine) {

  def putMapping(mapping: String): Future[Boolean] = {
    future {
      engine
        .client
        .admin()
        .indices()
        .preparePutMapping(indexName)
        .setType(typeName)
        .setSource(mapping)
        .execute()
        .get()
        .isAcknowledged
    }
  }

  def putMapping(mapping: Map[String, Map[String, String]]): Future[Boolean] = {
    putMapping(compact(render(Map(typeName -> Map("properties" -> mapping)))))
  }

  case class IndexResult(id: String, version: Long)

  def index(data: JValue, id: Option[String] = None): Future[IndexResult] = {
    future {
      val source = engine.client
        .prepareIndex(indexName, typeName)
        .setSource(compact(render(data)))
      val prepare = id match {
        case Some(i) => source.setId(i)
        case _ => source
      }
      val result = prepare.execute().get()

      IndexResult(result.getId, result.getVersion)
    }
  }

  def get(id: String): Future[Option[GetResponse]] = {
    future {
      val result = engine
        .client
        .prepareGet(indexName, typeName, id)
        .execute()
        .get()

      if (result.isExists) {
        Some(result)
      } else {
        None
      }
    }
  }

  def all(): Future[SearchResponse] = {
    search(QueryBuilders.matchAllQuery())
  }

  def search(query: QueryBuilder): Future[SearchResponse] = {
    future {
      engine
        .client
        .prepareSearch(indexName)
        .setQuery(query)
        .setTypes(typeName)
        .execute()
        .get()
    }
  }

}
