package etude.collaboration.github.infrastructure

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext
import java.net.URI

trait GithubEntityIOContext[M[+A]]
  extends EntityIOContext[M] {

  val githubUri: URI
}
