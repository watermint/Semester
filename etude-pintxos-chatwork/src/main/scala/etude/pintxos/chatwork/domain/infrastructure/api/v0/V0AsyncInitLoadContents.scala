package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.room.{Participant, Room}

case class V0AsyncInitLoadContents(contacts: List[Account],
                                   rooms: List[Room],
                                   participants: List[Participant])
