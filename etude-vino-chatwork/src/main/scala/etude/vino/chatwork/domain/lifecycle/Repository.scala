package etude.vino.chatwork.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.search.sort.SortBuilder
import org.json4s.JValue
import org.json4s.native.JsonMethods
import org.json4s.native.JsonMethods._

trait Repository[E <: Entity[ID], ID <: Identity[_]] {
  val engine: ElasticSearch

  def indexName(entity: E): String

  val typeName: String

  def fromJsonSeq(id: Option[String], source: JValue): Seq[E]

  def fromJson(id: Option[String], json: JValue): Option[E] = fromJsonSeq(id, json).lastOption

  def toJson(entity: E): JValue

  def toJsonString(entity: E): String = compact(render(toJson(entity)))

  def toIdentity(identity: ID): String

  def update(entity: E): Long = {
    engine.update(
      indexName(entity),
      typeName,
      toIdentity(entity.identity),
      toJson(entity)
    )
  }

  def delete(entity: E): Long = {
    engine.delete(
      indexName(entity),
      typeName,
      toIdentity(entity.identity)
    )
  }

  def get(identity: ID): Option[E]

  def search(indices: String,
             query: QueryBuilder,
             options: SearchOptions): SearchResult[E, ID] = {
    val reqWithQuery = engine.client
      .prepareSearch()
      .setIndices(indices)
      .setTypes(typeName)
      .setQuery(query)

    val reqWithSort = options.sort match {
      case Some(s) => reqWithQuery.addSort(s)
      case _ => reqWithQuery
    }

    val reqWithSize = options.size match {
      case Some(s) => reqWithSort.setSize(s)
      case _ => reqWithSort
    }

    val response = reqWithSize.execute().get()

    SearchResult[E, ID](
      response.getHits.hits() flatMap {
        h =>
          fromJson(Some(h.getId), JsonMethods.parse(h.getSourceAsString))
      }
    )
  }

}
