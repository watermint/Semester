package etude.vino.chatwork.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}
import org.elasticsearch.index.query.{FilterBuilder, QueryBuilder}
import org.json4s.native.JsonMethods

trait SimpleIndexRepository[E <: Entity[ID], ID <: Identity[_]] extends Repository[E, ID] {
  val indexName: String

  val typeName: String

  def indexName(entity: E): String = indexName

  def typeName(entity: E): String = typeName

  def get(identity: ID): Option[E] = {
    engine.get(
      indexName,
      typeName,
      toIdentity(identity)
    ) map {
      json =>
        fromJson(None, json)
    }
  }

  def search(query: QueryBuilder,
             postFilter: Option[FilterBuilder] = None): Seq[E] = {

    val reqWithQuery = engine.client
      .prepareSearch()
      .setIndices(indexName)
      .setTypes(typeName)
      .setQuery(query)

    val req = postFilter match {
      case Some(f) => reqWithQuery.setPostFilter(f)
      case _ => reqWithQuery
    }

    val response = req.execute().actionGet()

    response.getHits.hits() map {
      h =>
        fromJson(Some(h.getId), JsonMethods.parse(h.getSourceAsString))
    }
  }
}
