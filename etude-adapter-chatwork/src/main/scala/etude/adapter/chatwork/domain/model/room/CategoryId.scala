package etude.adapter.chatwork.domain.model.room

import etude.domain.core.model.Identity

case class CategoryId(categoryId: BigInt) extends Identity[BigInt] {
  def value: BigInt = categoryId
}
