name := "semester-foundation-logging"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "latest.release",
  "org.slf4j" % "jcl-over-slf4j" % "latest.release",
  "org.slf4j" % "jul-to-slf4j" % "latest.release",
  "org.slf4j" % "log4j-over-slf4j" % "latest.release",
  "ch.qos.logback" % "logback-classic" % "latest.release"
)
