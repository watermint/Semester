package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.{EntityIOContext, ResultWithIdentity}
import etude.pintxos.chatwork.domain.model.room.{Participant, RoomId}

import scala.concurrent.Future

private[room]
class AsyncParticipantRepositoryOnV1Api
  extends AsyncParticipantRepository {
  //  private val ENDPOINT_ROOMS = "/v1/rooms"
  //
  //  protected def parseRoomRole(roomId: RoomId, json: JValue): List[RoomRole] = {
  //    for {
  //      JObject(data) <- json
  //      JField("account_id", JInt(accountId)) <- data
  //      JField("role", JString(role)) <- data
  //    } yield {
  //      new RoomRole(
  //        new RoomRoleId(new AccountId(accountId), roomId),
  //        RoomRoleType(role)
  //      )
  //    }
  //  }
  //
  //  protected def parseUpdatedRoomRole(roomId: RoomId, json: JValue): List[RoomRole] = {
  //    def parse(roomId: RoomId, fieldName: String, role: RoomRoleType): List[RoomRole] = {
  //      for {
  //        JObject(data) <- json
  //        JField(fieldName, JArray(list)) <- data
  //        JInt(accountId) <- list
  //      } yield {
  //        new RoomRole(new RoomRoleId(new AccountId(accountId), roomId), role)
  //      }
  //    }
  //
  //    parse(roomId, "admin", RoomRoleAdmin()) ++
  //      parse(roomId, "member", RoomRoleMember()) ++
  //      parse(roomId, "readonly", RoomRoleReadonly())
  //  }
  //
  //  protected def updateRolesInRoom(roomId: RoomId, roomRoles: List[RoomRole]): Try[List[RoomRole]] = {
  //    val endPoint = s"$ENDPOINT_ROOMS/${roomId.value}/members"
  //    if (shouldFail(endPoint)) {
  //      return Failure(QoSException(endPoint))
  //    }
  //
  //    try {
  //      V1AsyncApi.put(
  //        path = endPoint,
  //        data = List(
  //          "members_admin_ids" -> roomRoles.filter(_.roleType.isInstanceOf[RoomRoleAdmin]).map(_.identity.accountId.value).mkString(","),
  //          "members_member_ids" -> roomRoles.filter(_.roleType.isInstanceOf[RoomRoleMember]).map(_.identity.accountId.value).mkString(","),
  //          "members_readonly_ids" -> roomRoles.filter(_.roleType.isInstanceOf[RoomRoleReadonly]).map(_.identity.accountId.value).mkString(",")
  //        )
  //      ) match {
  //        case Failure(f) => Failure(f)
  //        case Success(json) => Success(parseUpdatedRoomRole(roomId, json))
  //      }
  //    } finally {
  //      lastLoad.put(endPoint, Instant.now)
  //    }
  //  }
  //
  //  def updateRolesInRoom(roomRoles: List[RoomRole]): Try[List[RoomRole]] = {
  //    val roles = for {
  //      (roomId, roles) <- roomRoles.groupBy(_.identity.roomId)
  //    } yield {
  //      updateRolesInRoom(roomId, roles) match {
  //        case Failure(f) => return Failure(f)
  //        case Success(r) => r
  //      }
  //    }
  //
  //    Success(roles.toList.flatten)
  //  }
  //
  //  def rolesInRoom(roomId: RoomId): Try[List[RoomRole]] = {
  //    val endPoint = s"$ENDPOINT_ROOMS/${roomId.value}/members"
  //    if (shouldFail(endPoint)) {
  //      return Failure(QoSException(endPoint))
  //    }
  //
  //    try {
  //      V1AsyncApi.get(endPoint) match {
  //        case Failure(f) => Failure(f)
  //        case Success(json) => Success(parseRoomRole(roomId, json))
  //      }
  //    } finally {
  //      lastLoad.put(endPoint, Instant.now)
  //    }
  //  }
  //
  //  def resolve(identifier: RoomRoleId): Try[RoomRole] = {
  //    V1AccountRepository().me() match {
  //      case Failure(f) => Failure(f)
  //      case Success(me) =>
  //        V1RoomRepository().resolve(identifier.roomId) match {
  //          case Failure(f) => Failure(f)
  //          case Success(room) => Success(
  //            new RoomRole(
  //              new RoomRoleId(
  //                roomId = room.roomId,
  //                accountId = me.accountId
  //              ),
  //              roleType = room.roomRole
  //            )
  //          )
  //        }
  //    }
  //
  //  }
  type This <: AsyncParticipantRepositoryOnV1Api

  def containsByIdentity(identity: RoomId)
                        (implicit context: EntityIOContext[Future]): Future[Boolean] = {
    ???
  }

  def resolve(identity: RoomId)
             (implicit context: EntityIOContext[Future]): Future[Participant] = {
    ???
  }

  def deleteByIdentity(identity: RoomId)
                      (implicit context: EntityIOContext[Future]):
      Future[ResultWithIdentity[This, RoomId, Participant, Future]] = {
    ???
  }

  def store(entity: Participant)
           (implicit context: EntityIOContext[Future]):
      Future[ResultWithIdentity[This, RoomId, Participant, Future]] = {
    ???
  }
}
