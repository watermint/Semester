package etude.vino.chatwork.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}
import org.elasticsearch.index.query.{FilterBuilder, QueryBuilder, QueryBuilders}

trait MultiIndexRepository[E <: Entity[ID], ID <: Identity[_]] extends Repository[E, ID] {
  val indexNamePrefix: String

  def get(identity: ID): Option[E] = {
    search(QueryBuilders.matchQuery("_id", toIdentity(identity))).lastOption
  }

  def search(query: QueryBuilder,
             postFilter: Option[FilterBuilder] = None): Seq[E] = {

    search(s"$indexNamePrefix*", query, postFilter)
  }
}
