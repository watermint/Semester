name := "semester-foundation-fextile"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "latest.release",
  "com.typesafe.akka" %% "akka-slf4j" % "latest.release",
  "org.scalafx" %% "scalafx" % "latest.release",
  "org.controlsfx" % "controlsfx" % "latest.release"
)

fork in run := true

