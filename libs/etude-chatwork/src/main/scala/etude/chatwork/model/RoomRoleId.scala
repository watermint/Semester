package etude.chatwork.model

import etude.ddd.model.Identity

class RoomRoleId(val accountId: AccountId,
                 val roomId: RoomId)
  extends Identity[(BigInt, BigInt)] {

  def value: (BigInt, BigInt) = accountId.value -> roomId.value


}
