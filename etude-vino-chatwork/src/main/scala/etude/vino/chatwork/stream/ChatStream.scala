package etude.vino.chatwork.stream

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.{GetUpdateRequest, InitLoadRequest, LoadChatRequest}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.{ChatWorkResponse, GetUpdateResponse, InitLoadResponse, LoadChatResponse}
import etude.vino.chatwork.api.{PriorityNormal, ApiHub, ApiSubscriber, PriorityRealTime}

case class ChatStream(apiHub: ApiHub)
  extends ApiSubscriber {

  private val subscribers = AggregatedSubscriber()

  private val updateClockCycleInSeconds = 600

  private val scheduledExecutor: ScheduledThreadPoolExecutor = {
    val executor = new ScheduledThreadPoolExecutor(1)

    executor.scheduleAtFixedRate(new Runnable {
      def run(): Unit = {
        apiHub.enqueue(GetUpdateRequest())(PriorityRealTime)
      }
    }, updateClockCycleInSeconds, updateClockCycleInSeconds, TimeUnit.SECONDS)

    executor
  }

  def addSubscriber(subscriber: ChatSubscriber): Unit = {
    subscribers.addSubscriber(subscriber)
  }

  def removeSubscriber(subscriber: ChatSubscriber): Unit = {
    subscribers.removeSubscriber(subscriber)
  }

  def start(): Unit = {
    apiHub.addSubscriber(this)
    apiHub.enqueue(InitLoadRequest())(PriorityNormal)
  }

  def shutdown(): Unit = {
    apiHub.removeSubscriber(this)
    scheduledExecutor.shutdown()
  }

  def receive: PartialFunction[ChatWorkResponse, Unit] = {
    case r: InitLoadResponse =>
      r.contacts.foreach { c => subscribers.update(c) }
      r.participants.foreach { p => subscribers.update(p) }
      r.rooms.foreach { r => subscribers.update(r) }

    case r: GetUpdateResponse =>
      r.roomUpdateInfo foreach {
        room =>
          subscribers.update(room.roomId)
          apiHub.enqueue(LoadChatRequest(room.roomId))(PriorityNormal)
      }

    case r: LoadChatResponse =>
      subscribers.update(r.chatList)
      r.chatList.foreach(subscribers.update)
  }

}
