package stenographer

import com.twitter.finatra.FinatraServer
import stenographer.controllers.{Admin, Root, Assets}

object App extends FinatraServer {
  // This is Semester project structure specific configuration.
  System.setProperty("com.twitter.finatra.config.docRoot", "apps/stenographer/src/main/resources")

  register(new Assets())
  register(new Admin())
  register(new Root())
}
