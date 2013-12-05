package etude.commons.domain.calendar

import scala.util.{Success, Failure, Try}

object Aggregation {
  def aggregate[R, V](funcSeq: Map[R, () => Try[V]]): Try[Seq[V]] = {
    val results = funcSeq.map(f => f._1 -> f._2())

    if (results.forall(_._2.isSuccess)) {
      Success(results.values.map(_.get).toSeq)
    } else {
      Failure(AggregatedException(results.collect { case Pair(r, Failure(l)) => r -> l }))
    }
  }
}
