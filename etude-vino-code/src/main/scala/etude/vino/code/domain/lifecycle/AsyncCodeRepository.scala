package etude.vino.code.domain.lifecycle

import etude.epice.elasticsearch.Engine

import scala.concurrent.Future

trait AsyncCodeRepository extends CodeRepository[Future] {
  type This <: AsyncCodeRepository
}

object AsyncCodeRepository {
  def withEngine(engine: Engine): AsyncCodeRepository =
    new AsyncCodeRepositoryOnElasticSearch(engine)
}
