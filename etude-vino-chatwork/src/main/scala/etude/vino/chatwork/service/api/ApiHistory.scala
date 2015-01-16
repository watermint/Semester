package etude.vino.chatwork.service.api

import java.time.Instant

import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest

case class ApiHistory(timestamp: Instant, request: ChatWorkRequest)
