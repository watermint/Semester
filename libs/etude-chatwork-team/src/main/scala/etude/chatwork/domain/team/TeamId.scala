package etude.chatwork.domain.team

import etude.commons.domain.Identity

class TeamId(val teamId: String) extends Identity[String] {
  def value: String = teamId
}
