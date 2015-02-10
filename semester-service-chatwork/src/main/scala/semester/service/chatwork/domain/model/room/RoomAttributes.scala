package semester.service.chatwork.domain.model.room

case class RoomAttributes(sticky: Boolean,
                          unreadCount: BigInt,
                          mentionCount: BigInt,
                          myTaskCount: BigInt,
                          totalTaskCount: BigInt,
                          fileCount: BigInt,
                          totalMessageCount: Option[BigInt] = None,
                          currentSequence: Option[BigInt] = None,
                          readSequence: Option[BigInt] = None)
