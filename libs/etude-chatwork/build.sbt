name := "etude-chatwork"

version := "0.0.1"

libraryDependencies ++= Seq(
  "org.apache.httpcomponents" % "httpclient" % "4.3.1",
  "org.specs2" %% "specs2" % "2.2.3" % "test",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "junit" % "junit" % "4.7" % "test"
)
