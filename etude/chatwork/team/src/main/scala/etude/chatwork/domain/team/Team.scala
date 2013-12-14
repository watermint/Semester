package etude.chatwork.domain.team

import etude.chatwork.domain.account.Account
import etude.foundation.domain.Entity

/**
 * Team aggregates account(s) for teaming.
 * Team is not a direct entity of ChatWork.
 * This entity only used in etude-chatwork-team.
 */
class Team(val teamId: TeamId,
           val name: Option[String],
           val accounts: Seq[Account])
  extends Entity[TeamId] {

  val identity: TeamId = teamId
}
