package etude.adapter.chatwork.domain.model.message.text

import etude.adapter.chatwork.domain.model.account.AccountId

case class IconWithName(accountId: AccountId) extends Fragment {
  def render(): String = s"[piconname:${accountId.value}]"
}
