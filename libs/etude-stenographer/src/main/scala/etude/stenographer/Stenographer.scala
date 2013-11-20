package etude.stenographer

import etude.file.Dir
import scala.collection.mutable

/**
 * home -> contract(org, email) -> room
 * home -> elasticsearch -> index(roomId) -> type(schema) -> id (e.g. message id)
 *
 * @param home home directory for stenographer.
 */
case class Stenographer(home: Dir) {
  lazy val search: Search = Search(home.resolveDir("elasticsearch"))

  lazy val contracts: mutable.HashMap[String, Contract] = mutable.HashMap[String, Contract]()
}
