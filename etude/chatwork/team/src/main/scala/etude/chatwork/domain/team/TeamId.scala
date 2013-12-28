package etude.chatwork.domain.team

import etude.foundation.domain.model.Identity

class TeamId(val teamId: String) extends Identity[String] {
  def value: String = teamId
}
