package etude.pintxos.chatwork.domain.model.message.text

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.MessageId

case class Reply(accountId: AccountId,
                 messageId: MessageId) extends Fragment {
  def render(): String = s"[rp aid=${accountId.value} to=${messageId.roomId.value}-${messageId.messageId}]"
}
