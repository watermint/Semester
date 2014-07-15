package etude.table.arrabbiata.controller.message.room

import etude.table.arrabbiata.controller.message.MessageWithSession
import etude.table.arrabbiata.state.Session
import etude.table.arrabbiata.ui.UIActor
import etude.table.arrabbiata.ui.message.composite.StatusUpdate
import etude.table.arrabbiata.ui.message.composite.room.DisplayDiffAccounts
import etude.vino.chatwork.provisioning.{AccountProvisioning, ProvisioningPolicy}
import etude.pintxos.chatwork.domain.lifecycle.account.AsyncAccountRepository
import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.room.Room

import scala.concurrent.Future

case class MergeDiffList(base: Room, target: Room)
  extends MessageWithSession {

  def perform(session: Session): Unit = {
    val provisioning = new AccountProvisioning
    implicit val execContext = session.ioContext.executor
    implicit val ioContext = session.ioContext
    val accountRepo = AsyncAccountRepository.ofContext(session.ioContext)

    UIActor.ui ! StatusUpdate("Loading account(s)")

    provisioning.diff(target.roomId,
      base.roomId,
      ProvisioningPolicy.toRoleThenFromRole) map {
      diff =>
        val accounts = diff.map {
          accountId =>
            accountRepo.resolve(accountId) map {
              account =>
                account
            } fallbackTo {
              Future.successful(
                new Account(
                  accountId = accountId,
                  name = Some("id: " + accountId.value)
                )
              )
            }
        }
        Future.sequence(accounts).onSuccess {
          case diff: Seq[Account] =>
            UIActor.ui ! DisplayDiffAccounts(diff)
            UIActor.ui ! StatusUpdate("")
        }
    }
  }
}
