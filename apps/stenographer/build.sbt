name := "Stenographer"

version := "0.0.1"

resolvers ++= Seq(
  "maven-central" at "http://repo1.maven.org/maven2",
  "twitter-repo" at "http://maven.twttr.com"
)

libraryDependencies ++= Seq(
  "com.twitter" % "finatra" % "1.4.0",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.specs2" %% "specs2" % "2.2.3" % "test",
  "junit" % "junit" % "4.7" % "test"
)
