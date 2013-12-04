package etude.chatwork.domain.room

case class RoomAttributes(sticky: Boolean,
                          unreadCount: BigInt,
                          mentionCount: BigInt,
                          myTaskCount: BigInt,
                          totalTaskCount: BigInt,
                          totalMessageCount: BigInt,
                          fileCount: BigInt)
