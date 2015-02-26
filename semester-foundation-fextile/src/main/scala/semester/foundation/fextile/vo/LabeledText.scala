package semester.foundation.fextile.vo

import java.util.UUID

case class LabeledText(identity: UUID,
                       text: Option[String])
  extends UIValueObject
