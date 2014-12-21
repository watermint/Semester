package etude.vino.chatwork.stream

import java.nio.file.Paths

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.node.{NodeBuilder, Node}
import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

object MessageStreamStorage {
  val storagePath = Paths.get(System.getProperty("user.home"), ".etude-vino-chatwork")

  val useTransportClient = false

  def createEmbeddedNode: Node = {
    NodeBuilder
      .nodeBuilder()
      .clusterName("chatwork")
      .local(false)
      .settings(
        ImmutableSettings
          .settingsBuilder()
          .put("path.home", storagePath)
          .put("path.logs", storagePath.resolve("logs"))
          .put("http.enabled", true)
          .put("http.port", 9200)
          .put("http.cors.enabled", true)
          .put("http.cors.allow-origin", "/.*/")
      ).node()
  }

  lazy val client: Client = createEmbeddedNode.client()

  def messageHandler(message: Message): Unit = {

    val toAccount = message.body.to.map(_.value)
    val replies = message.body.replyTo.map(_.value)

    val json =
      ("@timestamp" -> message.ctime.toString) ~
      ("body" -> message.body.text) ~
      ("account" -> message.accountId.value) ~
      ("room" -> message.messageId.roomId.value) ~
      ("to" -> toAccount.toList) ~
      ("replyTo" -> replies)

    val response = client.prepareIndex()
      .setIndex("chatwork")
      .setType("message")
      .setId(s"${message.messageId.roomId}-${message.messageId.messageId}")
      .setTimestamp(message.ctime.toString)
      .setSource(compact(render(json)))
      .execute()
      .get()
  }

  def roomHandler(room: RoomId): Unit = {

  }

  def main(args: Array[String]) {
    client.admin().indices()
    val stream = MessageStream.fromThinConfig(messageHandler, roomHandler)
    stream.start()
  }
}
