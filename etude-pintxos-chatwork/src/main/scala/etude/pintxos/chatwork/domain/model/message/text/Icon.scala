package etude.pintxos.chatwork.domain.model.message.text

import etude.pintxos.chatwork.domain.model.account.AccountId

case class Icon(accountId: AccountId) extends Fragment {
  def render(): String = s"[picon:${accountId.value}]"
}
