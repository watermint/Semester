package etude.kitchenette.code.domain.lifecycle

import etude.domain.core.lifecycle.{ResultWithIdentity, EntityIOContext}
import etude.kitchenette.code.domain.model.{File, FileId}

import scala.concurrent.Future

trait AsyncFileRepository extends FileRepository[Future] {
  type This <: AsyncFileRepository
}

object AsyncFileRepository {

}
