package etude.chatwork.domain.message

import scala.util.Try
import etude.chatwork.domain.Repository
import etude.chatwork.domain.account.AccountRepository

trait MessageRepository
  extends Repository[MessageId, Message] {

  def messages(baseline: MessageId)(implicit accountRepository: AccountRepository): Try[List[Message]]

  def contains(entity: Message): Try[Boolean] = contains(entity.messageId)
}
