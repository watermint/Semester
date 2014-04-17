package etude.messaging.chatwork.domain.infrastructure.api

import scala.language.higherKinds
import etude.foundation.http.SyncClient
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.infrastructure.api.v0.V0UpdateSubscriber
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{SyncVar, Lock}
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

trait EntityIOContextOnV0Api[M[+A]]
  extends EntityIOContext[M] {

  val organizationId: Option[String]

  val username: String

  val password: String

  val client: SyncClient = SyncClient()

  val accessToken: SyncVar[String] = new SyncVar[String]

  val myId: SyncVar[String] = new SyncVar[String]

  val loginMutex: Lock = new Lock

  val loginTimestamp: SyncVar[Instant] = new SyncVar[Instant]

  val loginFailure: AtomicInteger = new AtomicInteger()

  /**
   * Update tracking id.
   */
  val lastId: SyncVar[String] = new SyncVar[String]

  val updateSubscribers: ArrayBuffer[V0UpdateSubscriber] = new ArrayBuffer[V0UpdateSubscriber]
}
