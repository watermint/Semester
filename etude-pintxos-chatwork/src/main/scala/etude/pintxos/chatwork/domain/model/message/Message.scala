package etude.pintxos.chatwork.domain.model.message

import java.time.Instant

import etude.manieres.domain.model.Entity
import etude.pintxos.chatwork.domain.model.account.AccountId

class Message(val messageId: MessageId,
              val accountId: AccountId,
              val body: Text,
              val ctime: Instant,
              val mtime: Option[Instant])
  extends Entity[MessageId]{

  val identity: MessageId = messageId
}
