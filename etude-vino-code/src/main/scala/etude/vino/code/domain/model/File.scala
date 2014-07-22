package etude.vino.code.domain.model

import etude.manieres.domain.model.Entity

class File(val fileId: FileId,
           val contentType: String) extends Entity[FileId] {
  val identity: FileId = fileId
}
