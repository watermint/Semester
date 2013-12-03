package etude.chatwork.v1

import java.time.Instant

case class Message(messageId: MessageId,
                   account: Account,
                   body: String,
                   ctime: Instant,
                   mtime: Option[Instant]) extends Entity[MessageId]
