package semester.application.vino.service.api

import java.time.Instant

import semester.service.chatwork.domain.service.request.ChatWorkRequest

case class ApiHistory(timestamp: Instant, request: ChatWorkRequest)
