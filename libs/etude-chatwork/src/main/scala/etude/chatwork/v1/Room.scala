package etude.chatwork.v1

import java.net.URI
import java.time.Instant

/**
 * @see http://developer.chatwork.com/ja/endpoint_rooms.html#GET-rooms
 */
case class Room(roomId: RoomId,
                name: String,
                description: String,
                roomType: RoomType,
                sticky: Boolean,
                unreadCount: BigInt,
                mentionCount: BigInt,
                myTaskCount: BigInt,
                totalTasksCount: BigInt,
                fileCount: BigInt,
                avatar: URI,
                lastUpdateTime: Instant) extends Entity[RoomId]
