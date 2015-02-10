package semester.application.vino.domain

import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse
import semester.application.vino.domain.infrastructure.ElasticSearch
import semester.application.vino.domain.lifecycle._
import semester.application.vino.domain.state._
import semester.application.vino.service.api.{Api, ApiHistory}

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
