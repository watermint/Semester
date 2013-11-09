package stenographer

import com.twitter.finatra.FinatraServer

object App extends FinatraServer {
  // This is Semester project structure specific configuration.
  System.setProperty("com.twitter.finatra.config.docRoot", "apps/stenographer/src/main/resources")

  register(new Bootstrap())
}
