package etude.pintxos.chatwork.domain.service.v0

import etude.epice.utility.qos.Throttle

object V0ApiQoS {
  lazy val throttle: Throttle = Throttle(maxQueryPerSecond = 2, randomWaitRangeSeconds = 2)
}
