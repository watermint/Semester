package etude.vino.chatwork

import java.util.concurrent.{ExecutorService, Executors}

import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.InitLoadRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.ChatWorkResponse
import etude.vino.chatwork.api.{PriorityNormal, ApiHub}
import etude.vino.chatwork.historian.Historian
import etude.vino.chatwork.recorder.Recorder
import etude.vino.chatwork.storage.Storage

import scala.concurrent.ExecutionContext

object Main {
  def main(args: Array[String]) {
    val executorsPool: ExecutorService = Executors.newCachedThreadPool()
    implicit val executors = ExecutionContext.fromExecutorService(executorsPool)
    implicit val context = AsyncEntityIOContextOnV0Api.fromThinConfig()

    try {
      Storage.client.admin().indices().prepareExists().setIndices("cw-vino-settings").execute().get()
    } catch {
      case _: Exception =>
        // ignore
    }

    val apiHub = ApiHub(context)

    apiHub.system.eventStream.subscribe(apiHub.system.actorOf(Historian.props(apiHub)), classOf[ChatWorkResponse])
    apiHub.system.eventStream.subscribe(apiHub.system.actorOf(Recorder.props(apiHub, 600)), classOf[ChatWorkResponse])

    apiHub.enqueue(InitLoadRequest())(PriorityNormal)
  }

}
