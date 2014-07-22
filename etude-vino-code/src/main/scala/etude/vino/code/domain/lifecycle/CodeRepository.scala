package etude.vino.code.domain.lifecycle

import etude.manieres.domain.lifecycle.Repository
import etude.vino.code.domain.model.{FileId, Code}

import scala.language.higherKinds

trait CodeRepository[M[+A]] extends Repository[FileId, Code, M]