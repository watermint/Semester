package etude.pintxos.nsunc

import etude.pain.foundation.{Foundation, ID}

object Notification {
  val center: ID = Foundation.invoke(Foundation.getObjcClass("NSUserNotificationCenter"),
    "defaultUserNotificationCenter")

  def notify(title: String, description: String) = {
    val notification: ID = Foundation.invoke(Foundation.getObjcClass("NSUserNotification"), "new")


    Foundation.invoke(notification, "setTitle:", Foundation.nsString(title.replace("%", "%%")))
    Foundation.invoke(notification, "setInformativeText:", Foundation.nsString(description.replace("%", "%%")))

    Foundation.invoke(center, "deliverNotification:", notification)
  }

  def cleanup() = {
    Foundation.invoke(center, "removeAllDeliveredNotifications")
  }
}
