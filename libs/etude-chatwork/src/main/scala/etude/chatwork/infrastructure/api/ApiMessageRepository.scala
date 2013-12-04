package etude.chatwork.infrastructure.api

import etude.chatwork.domain.message.{Message, MessageId, MessageRepository}
import scala.util.Try
import etude.chatwork.domain.account.AccountRepository
import etude.chatwork.infrastructure.api.v0.V0MessageRepository
import etude.chatwork.infrastructure.api.v1.V1MessageRepository

case class ApiMessageRepository(implicit authContext: MixedAuthContext)
  extends MessageRepository {

  def messages(baseline: MessageId)(implicit accountRepository: AccountRepository): Try[List[Message]] =
    V0MessageRepository().messages(baseline)

  def resolve(identifier: MessageId): Try[Message] =
    V1MessageRepository().resolve(identifier)
}
