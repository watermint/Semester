package etude.chatwork.v1

case class RoomAttributes(sticky: Boolean,
                          unreadCount: BigInt,
                          mentionCount: BigInt,
                          myTaskCount: BigInt,
                          totalTaskCount: BigInt,
                          totalMessageCount: BigInt,
                          fileCount: BigInt)
