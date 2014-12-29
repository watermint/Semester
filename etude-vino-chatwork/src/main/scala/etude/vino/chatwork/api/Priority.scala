package etude.vino.chatwork.api

sealed trait Priority

case object PriorityRealTime extends Priority

case object PriorityNormal extends Priority

case object PriorityLow extends Priority

case object PriorityLower extends Priority