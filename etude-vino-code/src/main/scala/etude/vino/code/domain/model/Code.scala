package etude.vino.code.domain.model

import etude.domain.core.model.Entity

class Code(val fileId: FileId,
           val code: String,
           val highlight: String) extends Entity[FileId] {
  val identity: FileId = fileId
}
