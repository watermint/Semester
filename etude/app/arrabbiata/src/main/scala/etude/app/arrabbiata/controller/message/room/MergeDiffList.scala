package etude.app.arrabbiata.controller.message.room

import etude.app.arrabbiata.controller.message.MessageWithSession
import etude.app.arrabbiata.state.Session
import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.room.DisplayDiffAccounts
import etude.kitchenette.fedelini.provisioning.{AccountProvisioning, ProvisioningPolicy}
import etude.messaging.chatwork.domain.lifecycle.account.AsyncAccountRepository
import etude.messaging.chatwork.domain.model.account.Account
import etude.messaging.chatwork.domain.model.room.Room

import scala.concurrent.Future

case class MergeDiffList(base: Room, target: Room)
  extends MessageWithSession {

  def perform(session: Session): Unit = {
    val provisioning = new AccountProvisioning
    implicit val execContext = session.ioContext.executor
    implicit val ioContext = session.ioContext
    val accountRepo = AsyncAccountRepository.ofContext(session.ioContext)

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
        }
    }
  }
}
