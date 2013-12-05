package etude.chatwork.domain.message

import java.time.Instant
import etude.chatwork.domain.account.Account
import etude.chatwork.domain.JSONSerializable
import etude.commons.domain.Entity

class Message(val messageId: MessageId,
              val account: Account,
              val body: String,
              val ctime: Instant,
              val mtime: Option[Instant])
  extends Entity[MessageId]
  with JSONSerializable {

  val identity: MessageId = messageId
}
