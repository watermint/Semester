package etude.elasticsearch

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.json4s.JValue

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

  case class GetResult(id: String, version: Long, data: JValue)

  def get(id: String): Future[GetResult] = {
    future {
      val result = engine
        .client
        .prepareGet(indexName, typeName, id)
        .execute()
        .get()

      GetResult(
        result.getId,
        result.getVersion,
        parse(result.getSourceAsString)
      )
    }
  }

  /*
   * Drop support due to error like below.
   * [error] error while loading ImmutableMapEntry, class file '<snip>(org/elasticsearch/common/collect/ImmutableMapEntry.class)' is broken
   */
  //  import scala.collection.JavaConverters._
  //  import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse
  //  def mapping(): Future[Map[String, Map[String, GetFieldMappingsResponse.FieldMappingMetaData]]] = {
  //    future {
  //      engine
  //        .client
  //        .admin()
  //        .indices()
  //        .prepareGetFieldMappings(typeName)
  //        .execute()
  //        .get()
  //        .mappings()
  //        .asScala
  //        .map { m => m._1 -> (m._2.asScala flatMap {
  //          m1 => m1._2.asScala
  //        })
  //      }
  //    }
  //  }


}
