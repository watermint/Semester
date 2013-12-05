package etude.commons.domain.file

import java.nio.file.{Path => JavaPath}

case class NoPermission(javaPath: JavaPath) extends InvalidPath