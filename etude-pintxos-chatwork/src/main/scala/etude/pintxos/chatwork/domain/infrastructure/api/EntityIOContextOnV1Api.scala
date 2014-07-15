package etude.pintxos.chatwork.domain.infrastructure.api

import etude.domain.core.lifecycle.EntityIOContext

import scala.language.higherKinds

trait EntityIOContextOnV1Api[M[+A]]
  extends EntityIOContext[M] {

  val token: String
}