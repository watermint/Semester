package etude.chatwork.model

import java.time.Instant
import etude.ddd.model.Entity

class Message(val messageId: MessageId,
              val account: Account,
              val body: String,
              val ctime: Instant,
              val mtime: Option[Instant])
  extends Entity[MessageId] {

  val identity: MessageId = messageId
}
