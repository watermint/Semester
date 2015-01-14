package etude.vino.chatwork.service.api

sealed trait Priority

case object PriorityHigh extends Priority

case object PriorityNormal extends Priority

case object PriorityLow extends Priority

case object PriorityLower extends Priority