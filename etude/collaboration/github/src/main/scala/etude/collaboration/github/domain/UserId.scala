package etude.collaboration.github.domain

import etude.foundation.domain.model.Identity

/**
 * @see http://developer.github.com/v3/users/
 */
case class UserId(id: BigInt)
  extends Identity[BigInt] {

  override def value: BigInt = id
}
