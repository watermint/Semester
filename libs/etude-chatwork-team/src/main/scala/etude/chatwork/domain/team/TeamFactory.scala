package etude.chatwork.domain.team

import etude.chatwork.domain.account.Account
import etude.commons.domain.Factory

trait TeamFactory extends Factory {
  def createTeam(accounts: Seq[Account],
                 name: Option[String] = None): Team
}
