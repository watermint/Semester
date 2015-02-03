package etude.vino.chatwork.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}
import org.elasticsearch.search.aggregations.Aggregation

case class SearchResult[E <: Entity[ID], ID <: Identity[_]](entities: Seq[E],
                                                            aggregations: Map[String, Aggregation])
