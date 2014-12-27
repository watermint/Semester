package etude.vino.chatwork.api

trait Priority

case object PriorityRealTime extends Priority

case object PriorityNormal extends Priority

case object PriorityLow extends Priority