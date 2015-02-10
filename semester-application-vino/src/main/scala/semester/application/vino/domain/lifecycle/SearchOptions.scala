package semester.application.vino.domain.lifecycle

import org.elasticsearch.action.search.SearchRequestBuilder
import org.elasticsearch.search.aggregations.AggregationBuilder
import org.elasticsearch.search.sort.SortBuilder

case class SearchOptions(sort: Option[SortBuilder] = None,
                         size: Option[Int] = None,
                         aggregations: Option[AggregationBuilder[_]] = None) {
  def withSort(request: SearchRequestBuilder): SearchRequestBuilder = {
    sort match {
      case Some(s) => request.addSort(s)
      case _ => request
    }
  }

  def withSize(request: SearchRequestBuilder): SearchRequestBuilder = {
    size match {
      case Some(s) => request.setSize(s)
      case _ => request
    }
  }

  def withAggregation(request: SearchRequestBuilder): SearchRequestBuilder = {
    aggregations match {
      case Some(a) => request.addAggregation(a)
      case _ => request
    }
  }

  def withOptions(request: SearchRequestBuilder): SearchRequestBuilder = {
    val options = Seq(
      withSort _,
      withSize _,
      withAggregation _
    )
    options.foldLeft(request)((r, opt) => opt(r))
  }
}
