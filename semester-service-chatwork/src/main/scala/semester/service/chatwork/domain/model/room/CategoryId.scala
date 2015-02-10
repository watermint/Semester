package semester.service.chatwork.domain.model.room

import semester.foundation.domain.model.Identity

case class CategoryId(categoryId: BigInt) extends Identity[BigInt] {
  def value: BigInt = categoryId
}
