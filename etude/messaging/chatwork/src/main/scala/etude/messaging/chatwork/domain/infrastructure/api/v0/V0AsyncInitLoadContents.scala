package etude.messaging.chatwork.domain.infrastructure.api.v0

import etude.messaging.chatwork.domain.model.account.Account
import etude.messaging.chatwork.domain.model.room.{Participant, Room}

case class V0AsyncInitLoadContents(contacts: List[Account],
                                   rooms: List[Room],
                                   participants: List[Participant])
