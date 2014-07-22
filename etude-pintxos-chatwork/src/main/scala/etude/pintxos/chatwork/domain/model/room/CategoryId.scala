package etude.pintxos.chatwork.domain.model.room

import etude.manieres.domain.model.Identity

case class CategoryId(categoryId: BigInt) extends Identity[BigInt] {
  def value: BigInt = categoryId
}
