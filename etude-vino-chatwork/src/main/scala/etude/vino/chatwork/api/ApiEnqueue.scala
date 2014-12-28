package etude.vino.chatwork.api

import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.ChatWorkRequest

case class ApiEnqueue(request: ChatWorkRequest, priority: Priority)
