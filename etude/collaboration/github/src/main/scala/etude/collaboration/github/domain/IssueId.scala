package etude.collaboration.github.domain

import etude.foundation.domain.model.Identity

/**
 * @see http://developer.github.com/v3/issues
 */
case class IssueId(number: BigInt)
  extends Identity[BigInt] {

  override def value: BigInt = number
}
