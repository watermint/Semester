name := "etude-gazpacho-logging"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "latest.integration",
  "org.slf4j" % "jcl-over-slf4j" % "latest.integration",
  "org.slf4j" % "jul-to-slf4j" % "latest.integration",
  "org.slf4j" % "log4j-over-slf4j" % "latest.integration",
  "ch.qos.logback" % "logback-classic" % "latest.integration"
)
