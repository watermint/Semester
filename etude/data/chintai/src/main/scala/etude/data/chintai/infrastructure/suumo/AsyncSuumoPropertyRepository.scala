package etude.data.chintai.infrastructure.suumo

import etude.data.chintai.domain.{Property, PropertyId, AsyncPropertyRepository}
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.Future

case class AsyncSuumoPropertyRepository()
  extends AsyncPropertyRepository {
  type This <: AsyncSuumoPropertyRepository

  def containsByIdentity(identity: PropertyId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executionContext = getExecutionContext(context)

    resolve(identity) map {
      p =>
        true
    }
  }

  def resolve(identity: PropertyId)(implicit context: EntityIOContext[Future]): Future[Property] = {
    implicit val executionContext = getExecutionContext(context)

    // //*[@id="contents"]/table/tbody/tr[2]/td[1]/ul/li[2]

    ???
  }
}
