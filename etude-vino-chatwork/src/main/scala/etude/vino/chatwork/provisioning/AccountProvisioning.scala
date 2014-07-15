package etude.vino.chatwork.provisioning

import etude.domain.core.lifecycle.EntityIOContext
import etude.domain.core.lifecycle.async.AsyncEntityIO
import etude.pintxos.chatwork.domain.lifecycle.room.AsyncParticipantRepository
import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.room.{AccountRole, AccountRoleType, Participant, RoomId}

import scala.concurrent.Future

class AccountProvisioning extends AsyncEntityIO {
  def roomRoleMapping(toRoomRoles: Seq[AccountRole], fromRoomRoles: Seq[AccountRole]): Seq[RoomRoleMapping] = {
    for {
      accountId <- toRoomRoles.map { r => r.accountId} ++ fromRoomRoles.map { r => r.accountId}
    } yield {
      RoomRoleMapping(
        accountId = accountId,
        toRoomRole = toRoomRoles.find(_.accountId.equals(accountId)).map { r => r.role},
        fromRoomRole = fromRoomRoles.find(_.accountId.equals(accountId)).map { r => r.role}
      )
    }
  }

  def mergedList(toRoom: Participant,
                 fromRoom: Participant,
                 mapper: (RoomRoleMapping) => Option[AccountRoleType]): Seq[AccountRole] = {
    roomRoleMapping(toRoom.roles, fromRoom.roles) flatMap {
      rrm =>
        mapper(rrm) match {
          case Some(role) => Some(AccountRole(rrm.accountId, role))
          case _ => None
        }
    }
  }

  def diff(toRoom: RoomId,
           fromRoom: RoomId,
           mapper: (RoomRoleMapping) => Option[AccountRoleType])
          (implicit context: EntityIOContext[Future]): Future[Seq[AccountId]] = {
    implicit val executionContext = getExecutionContext(context)
    val participantRepository = AsyncParticipantRepository.ofContext(context)

    for {
      toParticipant <- participantRepository.resolve(toRoom)
      fromParticipant <- participantRepository.resolve(fromRoom)
    } yield {
      val merged = mergedList(
        toParticipant,
        fromParticipant,
        mapper
      ) map (_.accountId)

      merged.distinct.diff(toParticipant.roles.map(_.accountId).distinct).distinct
    }
  }

  def merge(toRoom: RoomId,
            fromRoom: RoomId,
            mapper: (RoomRoleMapping) => Option[AccountRoleType])
           (implicit context: EntityIOContext[Future]): Future[Participant] = {
    implicit val executionContext = getExecutionContext(context)
    val participantRepository = AsyncParticipantRepository.ofContext(context)

    for {
      toParticipant <- participantRepository.resolve(toRoom)
      fromParticipant <- participantRepository.resolve(fromRoom)
      participant <- provision(
        toRoom = toParticipant,
        roles = mergedList(
          toParticipant,
          fromParticipant,
          mapper
        )
      )
    } yield {
      participant
    }
  }

  def provision(toRoom: Participant,
                roles: Seq[AccountRole])
               (implicit context: EntityIOContext[Future]): Future[Participant] = {
    implicit val executionContext = getExecutionContext(context)
    val participantRepository = AsyncParticipantRepository.ofContext(context)
    val r = toRoom.copy(roles)
    for {
      p <- participantRepository.store(r)
    } yield {
      r
    }
  }
}
