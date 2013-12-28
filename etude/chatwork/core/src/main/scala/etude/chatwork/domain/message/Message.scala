package etude.chatwork.domain.message

import java.time.Instant
import etude.chatwork.domain.account.AccountId
import etude.foundation.domain.model.Entity

class Message(val messageId: MessageId,
              val accountId: AccountId,
              val body: String,
              val ctime: Instant,
              val mtime: Option[Instant])
  extends Entity[MessageId]{

  val identity: MessageId = messageId
}
