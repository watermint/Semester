package etude.vino.code.domain.lifecycle

import etude.manieres.domain.lifecycle.{ResultWithIdentity, EntityIOContext}
import etude.vino.code.domain.model.{File, FileId}
import etude.epice.elasticsearch.Engine

import scala.concurrent.Future

trait AsyncFileRepository extends FileRepository[Future] {
  type This <: AsyncFileRepository
}

object AsyncFileRepository {
  def withEngine(engine: Engine): AsyncFileRepository =
    new AsyncFileRepositoryOnElasticSearch(engine)
}
