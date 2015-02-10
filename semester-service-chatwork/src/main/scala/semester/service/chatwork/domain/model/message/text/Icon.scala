package semester.service.chatwork.domain.model.message.text

import semester.service.chatwork.domain.model.account.AccountId

case class Icon(accountId: AccountId) extends Fragment {
  def render(): String = s"[picon:${accountId.value}]"
}
