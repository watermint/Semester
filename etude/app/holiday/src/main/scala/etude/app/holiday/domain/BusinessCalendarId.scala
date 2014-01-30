package etude.app.holiday.domain

import etude.foundation.domain.model.Identity

class BusinessCalendarId(val name: String) extends Identity[String] {
  def value: String = name
}
