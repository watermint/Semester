package semester.application.vino.service.api

import java.time.Instant

import semester.service.chatwork.domain.service.v0.request.ChatWorkRequest

case class ApiHistory(timestamp: Instant, request: ChatWorkRequest)
