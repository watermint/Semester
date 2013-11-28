package etude.chatwork

case class AccountId(accountId: String) {
  lazy val id: BigInt = BigInt(accountId)
}

object AccountId {
  def apply(accountId: BigInt): AccountId = AccountId(accountId.toString())
}