name := "semester-application-vino"

libraryDependencies ++= Seq(
  // require groovy for using kibana 4
  //  "org.codehaus.groovy" % "groovy-all" % "latest.release",
  "com.typesafe.akka" %% "akka-actor" % "latest.release",
  "com.typesafe.akka" %% "akka-slf4j" % "latest.release",
  "org.json4s" %% "json4s-native" % "latest.release",
  "org.elasticsearch" % "elasticsearch" % "latest.release",
  "org.elasticsearch" % "elasticsearch-analysis-kuromoji" % "latest.release",
  "org.scalafx" %% "scalafx" % "latest.release",
  "org.controlsfx" % "controlsfx" % "latest.release"
)

fork in run := true
