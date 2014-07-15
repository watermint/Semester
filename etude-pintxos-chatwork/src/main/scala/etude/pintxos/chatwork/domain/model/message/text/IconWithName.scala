package etude.pintxos.chatwork.domain.model.message.text

import etude.pintxos.chatwork.domain.model.account.AccountId

case class IconWithName(accountId: AccountId) extends Fragment {
  def render(): String = s"[piconname:${accountId.value}]"
}
