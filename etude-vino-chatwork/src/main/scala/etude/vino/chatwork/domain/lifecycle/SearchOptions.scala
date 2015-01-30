package etude.vino.chatwork.domain.lifecycle

import org.elasticsearch.search.sort.SortBuilder

case class SearchOptions(sort: Option[SortBuilder] = None,
                         size: Option[Int] = None)
