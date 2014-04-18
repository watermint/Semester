package etude.messaging.chatwork.domain.model.message

import java.time.Instant
import etude.messaging.chatwork.domain.model.account.AccountId
import etude.foundation.domain.model.Entity

class Message(val messageId: MessageId,
              val accountId: AccountId,
              val body: Text,
              val ctime: Instant,
              val mtime: Option[Instant])
  extends Entity[MessageId]{

  val identity: MessageId = messageId
}
