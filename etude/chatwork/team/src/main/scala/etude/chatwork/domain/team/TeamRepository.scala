package etude.chatwork.domain.team

import etude.foundation.domain.{EnumerableRepository, Repository}

trait TeamRepository
  extends Repository[TeamId, Team]
  with EnumerableRepository[TeamId, Team]
