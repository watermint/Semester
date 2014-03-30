package etude.messaging.chatwork.domain.infrastructure.api

import etude.foundation.utility.qos.Throttle

object ApiQoS {
  lazy val throttle: Throttle = Throttle(maxQueryPerSecond = 2, randomWaitRangeSeconds = 2)
}
