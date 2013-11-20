name := "Stenographer"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "com.twitter" % "finatra" % "1.4.0",
  "com.typesafe.akka" % "akka-actor_2.10" % "2.2.3",
  "org.elasticsearch" % "elasticsearch" % "0.90.6",
  "org.scala-lang" %% "scala-pickling" % "0.8.0-SNAPSHOT",
  "org.slf4j" % "slf4j-api" % "1.7.5"
)
