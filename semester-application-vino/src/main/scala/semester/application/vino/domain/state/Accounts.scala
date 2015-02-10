package semester.application.vino.domain.state

import akka.actor.{Actor, Props}
import semester.service.chatwork.domain.model.account.{Account, AccountId}
import semester.service.chatwork.domain.service.response.InitLoadResponse
import semester.application.vino.ui.UI

import scala.collection.mutable

class Accounts extends Actor {
  def receive: Receive = {
    case r: InitLoadResponse =>
      r.contacts.foreach {
        account =>
          self ! account
      }

    case a: Account =>
      Accounts.avatar.updateAvatar(a.accountId, a.avatarImage)
      Accounts.accounts.put(a.accountId, a)
  }
}

object Accounts {
  val accounts = new mutable.HashMap[AccountId, Account]()

  val avatar = new Avatar[AccountId]()

  def nameFor(accountId: AccountId): String = {
    accounts.get(accountId).flatMap(_.name).getOrElse(s"${accountId.value}")
  }

  val actorRef = UI.system.actorOf(Props[Accounts])
}
