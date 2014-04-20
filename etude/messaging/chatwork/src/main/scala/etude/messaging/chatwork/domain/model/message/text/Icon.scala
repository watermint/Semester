package etude.messaging.chatwork.domain.model.message.text

import etude.messaging.chatwork.domain.model.account.AccountId

case class Icon(accountId: AccountId) extends Fragment {
  def render(): String = s"[picon:${accountId.value}]"
}
