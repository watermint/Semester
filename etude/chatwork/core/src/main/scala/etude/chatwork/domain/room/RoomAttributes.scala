package etude.chatwork.domain.room

import etude.foundation.domain.model.ValueObject

case class RoomAttributes(sticky: Boolean,
                          unreadCount: BigInt,
                          mentionCount: BigInt,
                          myTaskCount: BigInt,
                          totalTaskCount: BigInt,
                          totalMessageCount: BigInt,
                          fileCount: BigInt)
  extends ValueObject
