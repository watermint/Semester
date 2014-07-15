package etude.pintxos.chatwork.domain.model.room

object AccountRoleType {
  case object Admin extends AccountRoleType
  case object Member extends AccountRoleType
  case object Readonly extends AccountRoleType
}

sealed abstract class AccountRoleType
