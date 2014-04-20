package etude.messaging.chatwork.domain.model.message.text

import etude.messaging.chatwork.domain.model.account.AccountId

case class To(accountId: AccountId) extends Fragment {
  def render(): String = s"[To:${accountId.value}]"
}

