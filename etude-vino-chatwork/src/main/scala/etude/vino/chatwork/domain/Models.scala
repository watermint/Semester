package etude.vino.chatwork.domain

import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import etude.vino.chatwork.domain.lifecycle._
import etude.vino.chatwork.domain.state._
import etude.vino.chatwork.service.api.{Api, ApiHistory}

object Models {
  val engine = ElasticSearch()

  val accountRepository: AccountRepository = AccountRepository(engine)

  val messageRepository: MessageRepository = MessageRepository(engine)

  val participantRepository: ParticipantRepository = ParticipantRepository(engine)

  val roomRepository: RoomRepository = RoomRepository(engine)

  val roomChunkRepository: RoomChunkRepository = RoomChunkRepository(engine)

  val markAsReadRepository: MarkAsReadRepository = MarkAsReadRepository(engine)

  def startup(): Unit = {
    Api.system.eventStream.subscribe(Accounts.actorRef, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(Rooms.actorRef, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(Messages.actorRef, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(Session.actorRef, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(ApplicationLog.actorRef, classOf[ApiHistory])
  }
}
