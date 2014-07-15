package etude.test

import java.util.Properties

import org.specs2.execute.Result

package object undisclosed {
  def undisclosed(name: String)
                 (f: Properties => Result): Result = {
    Undisclosed(name).undisclosed(f)
  }
}
