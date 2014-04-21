package etude.bookmark.pocket.domain.infrastructure

import scala.concurrent.ExecutionContext
import scala.util.Try
import java.util.concurrent.{Executors, ExecutorService}
import etude.bookmark.pocket.domain.infrastructure.auth.AuthSession

case class EntityIOContextOnApi(consumerKey: String,
                                accessToken: String,
                                executionContext: ExecutionContext)

object EntityIOContextOnApi {
  def fromDefault(): Try[EntityIOContextOnApi] = {
    val executorsPool: ExecutorService = Executors.newCachedThreadPool()
    val executors = ExecutionContext.fromExecutorService(executorsPool)

    AuthSession.loadSession() map {
      session =>
        EntityIOContextOnApi(
          session.consumerKey,
          session.accessToken,
          executors
        )
    }
  }
}