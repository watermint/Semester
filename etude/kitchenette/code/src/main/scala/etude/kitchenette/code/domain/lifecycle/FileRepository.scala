package etude.kitchenette.code.domain.lifecycle

import etude.kitchenette.code.domain.model.{File, FileId}

import scala.language.higherKinds
import etude.domain.core.lifecycle.Repository

trait FileRepository[M[+A]] extends Repository[FileId, File, M]
