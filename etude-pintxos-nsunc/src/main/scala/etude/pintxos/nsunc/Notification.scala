package etude.pintxos.nsunc

import etude.pain.foundation.{Foundation, ID}

object Notification {
  val center: ID = Foundation.invoke(Foundation.getObjcClass("NSUserNotificationCenter"),
    "defaultUserNotificationCenter")

  private def escapeText(text: String): String = {
    // Original code uses StringUtil#stripHtml which equivalent like below.
    //    text.replaceAll("<br/?>", "\n\n").replaceAll("<(.|\n)*?>", "").replace("%", "%%")
    text.replace("%", "%%")
  }

  /**
   * @param title title.
   * @param description description.
   * @see @see https://developer.apple.com/library/mac/documentation/Foundation/Reference/NSUserNotification_Class/Reference/Reference.html#//apple_ref/doc/uid/TP40012259
   */
  def notify(title: String, description: String): Unit = {
    val notification: ID = Foundation.invoke(Foundation.getObjcClass("NSUserNotification"), "new")

    Foundation.invoke(notification, "setTitle:", Foundation.nsString(escapeText(title)))
    Foundation.invoke(notification, "setInformativeText:", Foundation.nsString(escapeText(description)))

    Foundation.invoke(center, "deliverNotification:", notification)
  }

  /**
   * Clean up delivered notifications.
   */
  def cleanup(): Unit = {
    Foundation.invoke(center, "removeAllDeliveredNotifications")
  }
}
