package etude.stenographer

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx

/**
 *
 */
object Kitchensink {
  lazy val db = new ODatabaseDocumentTx("local:/tmp/orientdbtest")

  def main() = {
    if (db.exists()) {
      db.open("admin", "admin")
    } else {
      db.create()
    }
    db
  }
}
