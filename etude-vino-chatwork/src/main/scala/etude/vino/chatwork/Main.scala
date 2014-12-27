package etude.vino.chatwork

import java.util.concurrent.{ExecutorService, Executors}

import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.vino.chatwork.api.ApiHub
import etude.vino.chatwork.historian.Historian
import etude.vino.chatwork.recorder.Recorder
import etude.vino.chatwork.storage.Storage
import etude.vino.chatwork.stream.ChatStream

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
    val historian = Historian(apiHub)
    val chatStream = ChatStream(apiHub)
    val recorder = Recorder()

    chatStream.addSubscriber(recorder)
    chatStream.start()

  }

}
