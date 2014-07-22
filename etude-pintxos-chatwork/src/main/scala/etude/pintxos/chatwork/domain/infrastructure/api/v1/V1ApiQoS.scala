package etude.pintxos.chatwork.domain.infrastructure.api.v1


import etude.epice.utility.qos.Throttle

object V1ApiQoS {
  lazy val throttle: Throttle = Throttle(maxQueryPerSecond = 2, randomWaitRangeSeconds = 2)
}
