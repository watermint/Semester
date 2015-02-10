package semester.application.vino.service.api

import semester.service.chatwork.domain.service.v0.request.ChatWorkRequest

case class ApiEnqueue(request: ChatWorkRequest, priority: Priority)
