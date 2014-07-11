package etude.messaging.chatwork.domain.model.room

import etude.domain.core.model.Entity
import etude.messaging.chatwork.domain.model.account.AccountId

class Participant(val roomId: RoomId,
                  val admin: Seq[AccountId],
                  val member: Seq[AccountId],
                  val readonly: Seq[AccountId])
  extends Entity[RoomId] {

  val identity: RoomId = roomId

  lazy val roles: Seq[AccountRole] = {
    (admin map { a => AccountRole(a, AccountRoleType.Admin)}) ++
      (member map { a => AccountRole(a, AccountRoleType.Member)}) ++
      (readonly map { a => AccountRole(a, AccountRoleType.Readonly)})
  }

  def copy(roles: Seq[AccountRole]): Participant = {
    new Participant(
      roomId = roomId,
      admin = roles.filter(_.role == AccountRoleType.Admin).map { r => r.accountId },
      member = roles.filter(_.role == AccountRoleType.Member).map { r => r.accountId },
      readonly = roles.filter(_.role == AccountRoleType.Readonly).map { r => r.accountId }
    )
  }

  def copy(admin: Seq[AccountId] = this.admin,
           member: Seq[AccountId] = this.member,
           readonly: Seq[AccountId] = this.readonly): Participant = {
    new Participant(
      roomId = roomId,
      admin = admin,
      member = member,
      readonly = readonly
    )
  }
}

