package etude.epice.sel.decomposer

import etude.epice.sel.response.Response

import scala.util.Try

trait Decomposer[RS <: Response, NRS <: DecomposedResponse] {
  def extract(response: RS): Try[NRS]
}
