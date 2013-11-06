name := "etude-html"

version := "0.0.1"

libraryDependencies ++= Seq(
  "nu.validator.htmlparser" % "htmlparser" % "1.4",
  "org.specs2" %% "specs2" % "2.2.3" % "test",
  "junit" % "junit" % "4.7" % "test"
)
