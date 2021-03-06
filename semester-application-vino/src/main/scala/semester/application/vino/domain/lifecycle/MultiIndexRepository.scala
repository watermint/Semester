package semester.application.vino.domain.lifecycle

import semester.foundation.domain.model.{Entity, Identity}
import org.elasticsearch.index.query.{QueryBuilder, QueryBuilders}

trait MultiIndexRepository[E <: Entity[ID], ID <: Identity[_]] extends Repository[E, ID] {
  val indexNamePrefix: String

  def get(identity: ID): Option[E] = {
    search(QueryBuilders.matchQuery("_id", toIdentity(identity))).entities.lastOption
  }

  def search(query: QueryBuilder, options: SearchOptions = SearchOptions()): SearchResult[E, ID] = {
    search(s"$indexNamePrefix*", query, options)
  }
}
