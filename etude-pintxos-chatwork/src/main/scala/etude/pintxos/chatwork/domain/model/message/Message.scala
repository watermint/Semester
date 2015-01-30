package etude.pintxos.chatwork.domain.model.message

import java.net.URI
import java.time.Instant

import etude.epice.http._
import etude.manieres.domain.model.Entity
import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}

class Message(val messageId: MessageId,
              val accountId: AccountId,
              val body: Text,
              val ctime: Instant,
              val mtime: Option[Instant])
  extends Entity[MessageId]{

  val identity: MessageId = messageId

  def uri(implicit context: ChatWorkIOContext): URI = {
    ChatWorkApi.baseUri(context)
      .withFragment(s"!rid${messageId.roomId.value}-${messageId.messageId}")
  }
}
