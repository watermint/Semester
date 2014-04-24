package etude.messaging.chatwork.domain.model.room

import etude.foundation.domain.model.ValueObject

case class RoomAttributes(sticky: Boolean,
                          unreadCount: BigInt,
                          mentionCount: BigInt,
                          myTaskCount: BigInt,
                          totalTaskCount: BigInt,
//                          totalMessageCount: BigInt, // drop due to difficulties in V0 api.
                          fileCount: BigInt)
  extends ValueObject
