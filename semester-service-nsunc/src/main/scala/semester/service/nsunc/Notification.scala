package semester.service.nsunc

import semester.readymade.cf.{Foundation, ID}

case class Notification(title: String,
                        subTitle: Option[String] = None,
                        description: Option[String] = None,
                        soundName: Option[String] = None)

object Notification {
  val center: ID = Foundation.invoke(
    Foundation.getObjcClass("NSUserNotificationCenter"),
    "defaultUserNotificationCenter")

  private def escapeText(text: String): String = {
    // Original code uses StringUtil#stripHtml which equivalent like below.
    //    text.replaceAll("<br/?>", "\n\n").replaceAll("<(.|\n)*?>", "").replace("%", "%%")
    text.replace("%", "%%")
  }

  /**
   * @see https://developer.apple.com/library/mac/documentation/Foundation/Reference/NSUserNotification_Class/Reference/Reference.html#//apple_ref/doc/uid/TP40012259
   */
  def notify(n: Notification): Unit = {
    val notification: ID = Foundation.invoke(Foundation.getObjcClass("NSUserNotification"), "new")

    def addTextProperty(name: String, value: String) = {
      Foundation.invoke(notification, name, Foundation.nsString(escapeText(value)))
    }

    addTextProperty("setTitle:", n.title)
    n.subTitle.foreach(t => addTextProperty("setSubtitle:", t))
    n.description.foreach(t => addTextProperty("setInformativeText:", t))
    n.soundName.foreach(t => addTextProperty("setSoundName:", t))

    Foundation.invoke(center, "deliverNotification:", notification)
  }

  /**
   * Clean up delivered notifications.
   */
  def cleanup(): Unit = {
    Foundation.invoke(center, "removeAllDeliveredNotifications")
  }
}
