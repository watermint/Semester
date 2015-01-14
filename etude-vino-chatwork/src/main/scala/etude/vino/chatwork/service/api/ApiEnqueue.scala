package etude.vino.chatwork.service.api

import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest

case class ApiEnqueue(request: ChatWorkRequest, priority: Priority)
