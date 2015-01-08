package etude.pintxos.chatwork.domain.infrastructure.api

import java.util.concurrent.atomic.AtomicReference

import etude.epice.http.Client
import etude.manieres.domain.lifecycle.EntityIOContext

import scala.concurrent.SyncVar
import scala.language.higherKinds

trait EntityIOContextOnV0Api[M[+A]]
  extends EntityIOContext[M] {

  val organizationId: Option[String]

  val username: String

  val password: String

  var client: Client = Client()

  val accessToken: SyncVar[String] = new SyncVar[String]

  val myId: SyncVar[String] = new SyncVar[String]

  val lastId: AtomicReference[String] = new AtomicReference[String]
}
