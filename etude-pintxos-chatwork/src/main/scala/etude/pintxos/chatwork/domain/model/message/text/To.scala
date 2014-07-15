package etude.pintxos.chatwork.domain.model.message.text

import etude.pintxos.chatwork.domain.model.account.AccountId

case class To(accountId: AccountId) extends Fragment {
  def render(): String = s"[To:${accountId.value}]"
}

