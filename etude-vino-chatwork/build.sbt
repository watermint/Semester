name := "etude-vino-chatwork"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "latest.release",
  "org.json4s" %% "json4s-native" % "latest.release",
  "org.elasticsearch" % "elasticsearch" % "latest.release",
  "org.elasticsearch" % "elasticsearch-analysis-kuromoji" % "latest.release",
  "org.codehaus.groovy" % "groovy-all" % "latest.release"
)

