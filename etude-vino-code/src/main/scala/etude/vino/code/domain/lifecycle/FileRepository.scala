package etude.vino.code.domain.lifecycle

import etude.vino.code.domain.model.{File, FileId}

import scala.language.higherKinds
import etude.manieres.domain.lifecycle.Repository

trait FileRepository[M[+A]] extends Repository[FileId, File, M]
