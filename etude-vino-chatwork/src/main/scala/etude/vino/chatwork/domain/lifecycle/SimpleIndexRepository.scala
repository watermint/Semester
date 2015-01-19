package etude.vino.chatwork.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.search.aggregations.AggregationBuilder

trait SimpleIndexRepository[E <: Entity[ID], ID <: Identity[_]] extends Repository[E, ID] {
  val indexName: String

  def indexName(entity: E): String = indexName

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

  def search(query: QueryBuilder): SearchResult[E, ID] = {
    search(indexName, query)
  }

}
