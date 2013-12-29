package etude.test

import org.specs2.execute.Result
import java.util.Properties

package object undisclosed {
  def undisclosed(name: String)
                 (f: Properties => Result): Result = {
    Undisclosed(name).undisclosed(f)
  }
}
