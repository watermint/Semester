package etude.chatwork.domain.room

import etude.chatwork.domain.account.AccountId
import etude.foundation.domain.Identity

class RoomRoleId(val accountId: AccountId,
                 val roomId: RoomId)
  extends Identity[(BigInt, BigInt)] {

  def value: (BigInt, BigInt) = accountId.value -> roomId.value
}
