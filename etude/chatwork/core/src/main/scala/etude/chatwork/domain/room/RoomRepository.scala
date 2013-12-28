package etude.chatwork.domain.room

import scala.language.higherKinds

trait RoomRepository[M[+A]]
  extends RoomReader[M] {

}
