name := "Areca"

resolvers ++= Seq(
  "sonatype-public" at "https://oss.sonatype.org/content/groups/public",
  "time-sugar" at "http://watermint.github.io/time-sugar/mvn"
)

libraryDependencies ++= Seq(
  "com.github.scala-incubator.io" % "scala-io-file_2.10" % "0.4.2",
  "com.github.scopt" %% "scopt" % "3.1.0",
  "com.typesafe" % "config" % "1.0.2",
  "nu.validator.htmlparser" % "htmlparser" % "1.4",
  "org.watermint" % "time-sugar" % "r2",
  "org.specs2" %% "specs2" % "2.2.3" % "test",
  "junit" % "junit" % "4.7" % "test"
)
