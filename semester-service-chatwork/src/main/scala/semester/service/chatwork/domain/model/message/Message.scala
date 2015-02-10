package semester.service.chatwork.domain.model.message

import java.net.URI
import java.time.Instant

import semester.foundation.http._
import semester.foundation.domain.model.Entity
import semester.service.chatwork.domain.model.account.AccountId
import semester.service.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}

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
