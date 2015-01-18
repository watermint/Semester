package etude.vino.chatwork.domain

import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import etude.vino.chatwork.domain.lifecycle.{RoomRepository, ParticipantRepository, MessageRepository, AccountRepository}
import etude.vino.chatwork.domain.state.{Rooms, ApplicationLog, Accounts}
import etude.vino.chatwork.service.api.{Api, ApiHistory}

object Models {
  val engine = ElasticSearch()

  val accountRepository: AccountRepository = AccountRepository(engine)

  val messageRepository: MessageRepository = MessageRepository(engine)

  val participantRepository: ParticipantRepository = ParticipantRepository(engine)

  val roomRepository: RoomRepository = RoomRepository(engine)

  def startup(): Unit = {
    Api.system.eventStream.subscribe(Accounts.actorRef, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(Rooms.actorRef, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(ApplicationLog.actorRef, classOf[ApiHistory])
  }
}
