package etude.pintxos.chatwork.domain.infrastructure.api

import java.time.Instant
import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.{ScheduledFuture, ScheduledThreadPoolExecutor}

import etude.epice.http.Client
import etude.manieres.domain.lifecycle.EntityIOContext

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Lock, SyncVar}
import scala.language.higherKinds

trait EntityIOContextOnV0Api[M[+A]]
  extends EntityIOContext[M] {

  val organizationId: Option[String]

  val username: String

  val password: String

  var client: Client = Client()

  val accessToken: SyncVar[String] = new SyncVar[String]

  val myId: SyncVar[String] = new SyncVar[String]

  val loginMutex: ReentrantLock = new ReentrantLock

  val loginTimestamp: SyncVar[Instant] = new SyncVar[Instant]

  val loginFailure: AtomicInteger = new AtomicInteger()

  /**
   * Update tracking id.
   */
  val lastId: AtomicReference[String] = new AtomicReference[String]
}
