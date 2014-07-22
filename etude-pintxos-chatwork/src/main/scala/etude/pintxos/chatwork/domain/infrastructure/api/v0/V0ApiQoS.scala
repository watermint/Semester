package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.epice.utility.qos.Throttle

object V0ApiQoS {
  lazy val throttle: Throttle = Throttle(maxQueryPerSecond = 2, randomWaitRangeSeconds = 2)
}
