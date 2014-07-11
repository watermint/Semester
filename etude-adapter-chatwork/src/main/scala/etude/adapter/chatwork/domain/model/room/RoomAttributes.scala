package etude.adapter.chatwork.domain.model.room

import etude.domain.core.model.ValueObject

case class RoomAttributes(sticky: Boolean,
                          unreadCount: BigInt,
                          mentionCount: BigInt,
                          myTaskCount: BigInt,
                          totalTaskCount: BigInt,
//                          totalMessageCount: BigInt, // drop due to difficulties in V0 api.
                          fileCount: BigInt)
  extends ValueObject
