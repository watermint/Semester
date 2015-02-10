package semester.service.domain.infrastructure

import java.util.concurrent.{ExecutorService, Executors}

import semester.service.domain.infrastructure.auth.AuthSession

import scala.concurrent.ExecutionContext
import scala.util.Try

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