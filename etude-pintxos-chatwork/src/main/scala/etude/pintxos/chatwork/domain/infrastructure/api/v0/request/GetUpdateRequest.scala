package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

case class GetUpdateRequest(updateLastId: Boolean = true)
  extends ChatWorkRequest

