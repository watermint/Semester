package etude.vino.chatwork.ui.state

import java.net.URI

import akka.actor.{Props, Actor}
import etude.pintxos.chatwork.domain.model.account.{Account, AccountId}
import etude.pintxos.chatwork.domain.service.v0.response.InitLoadResponse
import etude.vino.chatwork.ui.UI
import etude.vino.chatwork.ui.pane.AccountListPane

import scala.collection.mutable

class Accounts extends Actor {
  def receive: Receive = {
    case r: InitLoadResponse =>
      r.contacts.foreach {
        account =>
          self ! account
      }
      UI.ref ! AccountListPane.AccountListUpdate(r.contacts)

    case a: Account =>
      Accounts.avatar.updateAvatar(a.accountId, a.avatarImage)
      Accounts.accounts.put(a.accountId, a)
  }
}

object Accounts {
  val accounts = new mutable.HashMap[AccountId, Account]()

  val avatar = new Avatar[AccountId]()

  def nameFor(accountId: AccountId): String = {
    accounts.get(accountId).flatMap(_.name).getOrElse(s"$accountId")
  }

  val actorRef = UI.system.actorOf(Props[Accounts])
}
