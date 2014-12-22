package etude.vino.chatwork.stream

import java.net.URI
import java.nio.file.Paths
import java.time.ZoneOffset

import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.{Participant, Room, RoomId}
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.{Node, NodeBuilder}
import org.json4s.JsonAST.JValue
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object ChatStreamStorage extends ChatSubscriber {
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
          .put("index.analysis.analyzer.default.type", "custom")
          .put("index.analysis.analyzer.default.tokenizer", "kuromoji_tokenizer")
          .put("http.enabled", true)
          .put("http.port", 9200)
          .put("http.cors.enabled", true)
          .put("http.cors.allow-origin", "/.*/")
      ).node()
  }

  lazy val client: Client = createEmbeddedNode.client()

  private def store(indexName: String,
                    typeName: String,
                    idName: String,
                    source: JValue): Long = {

    val response = client.prepareIndex()
      .setIndex(indexName)
      .setType(typeName)
      .setId(idName)
      .setSource(compact(render(source)))
      .execute()
      .get()

    response.getVersion
  }

  override def update(message: Message): Unit = {
    val toAccount = message.body.to.map(_.value)
    val replies = message.body.replyTo.map(_.value)

    val json =
      ("@timestamp" -> message.ctime.toString) ~
        ("body" -> message.body.text) ~
        ("account" -> message.accountId.value) ~
        ("room" -> message.messageId.roomId.value) ~
        ("to" -> toAccount.toList) ~
        ("replyTo" -> replies)

    val indexDate = message.ctime.atOffset(ZoneOffset.UTC).toLocalDate.toString
    val indexName = s"cw-message-$indexDate"

    store(
      indexName = indexName,
      typeName = "message",
      idName = s"${message.messageId.roomId.value}-${message.messageId.messageId}",
      source = json
    )
  }

  override def update(room: RoomId): Unit = {
    // NOP
  }

  override def update(room: Room): Unit = {
    val json =
      ("roomId" -> room.roomId.value) ~
        ("roomType" -> room.roomType.name) ~
        ("name" -> room.name) ~
        ("avatarUrl" -> room.avatar.getOrElse(new URI("")).toString) ~
        ("description" -> room.description.getOrElse(""))

    store(
      indexName = "cw-room",
      typeName = "room",
      idName = s"${room.roomId.value}",
      source = json
    )
  }

  override def update(account: Account): Unit = {
    val json =
      ("accountId" -> account.accountId.value) ~
        ("name" -> account.name.getOrElse("")) ~
        ("department" -> account.department.getOrElse("")) ~
        ("avatarUrl" -> account.avatarImage.getOrElse(new URI("")).toString)

    store(
      indexName = "cw-account",
      typeName = "account",
      idName = s"${account.accountId.value}",
      source = json
    )
  }

  override def update(participant: Participant): Unit = {
    val json =
      ("roomId" -> participant.roomId.value) ~
        ("admin" -> participant.admin.map(_.value)) ~
        ("readonly" -> participant.readonly.map(_.value)) ~
        ("member" -> participant.member.map(_.value))

    store(
      indexName = "cw-participant",
      typeName = "participant",
      idName = s"${participant.roomId.value}",
      source = json
    )
  }

  def main(args: Array[String]) {
    val stream = ChatStream.fromThinConfig()
    stream.addSubscriber(this)
    stream.start()
  }
}
