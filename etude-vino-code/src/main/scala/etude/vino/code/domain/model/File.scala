package etude.vino.code.domain.model

import etude.domain.core.model.Entity

class File(val fileId: FileId,
           val contentType: String) extends Entity[FileId] {
  val identity: FileId = fileId
}
