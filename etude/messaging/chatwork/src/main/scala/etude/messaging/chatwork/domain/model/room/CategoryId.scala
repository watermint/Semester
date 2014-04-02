package etude.messaging.chatwork.domain.model.room

import etude.foundation.domain.model.Identity

case class CategoryId(categoryId: BigInt) extends Identity[BigInt] {
  def value: BigInt = categoryId
}
