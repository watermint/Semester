package etude.app.culvert

import com.twitter.finatra.FinatraServer

object App extends FinatraServer {
  register(new Hook)
}
