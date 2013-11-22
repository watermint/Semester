name := "Semester"

lazy val buildSettings = Seq(
  version := "0.0.1",
  organization := "org.watermint",
  scalaVersion := "2.10.3",
  resolvers ++= Seq(
    "sonatype-public" at "https://oss.sonatype.org/content/groups/public",
    "atlassian-public" at "https://maven.atlassian.com/repository/public",
    "maven-central" at "http://repo1.maven.org/maven2",
    "twitter-repo" at "http://maven.twttr.com",
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.0.13",
    "org.slf4j" % "slf4j-api" % "1.7.5",
    "org.specs2" %% "specs2" % "2.2.3" % "test",
    "junit" % "junit" % "4.7" % "test"
  )
)


lazy val appsAreca = project.in(file("apps/areca"))
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)
  .dependsOn(etudeMoney)
  .dependsOn(etudeHtml)

lazy val appsPunchedTape = project.in(file("apps/punchedtape"))
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)
  .dependsOn(etudeChatwork)
  .dependsOn(etudeFile)

lazy val appsStenographer = project.in(file("apps/stenographer"))
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)
  .dependsOn(etudeBootstrap)
  .dependsOn(etudeChatwork)
  .dependsOn(etudeFile)
  .dependsOn(etudeStenographer)

lazy val etudeAggregation = project.in(file("libs/etude-aggregation"))
  .settings(buildSettings: _*)

lazy val etudeBootstrap = project.in(file("libs/etude-bootstrap"))
  .settings(buildSettings: _*)

lazy val etudeCalendar = project.in(file("libs/etude-calendar"))
  .settings(buildSettings: _*)
  .dependsOn(etudeAggregation)
  .dependsOn(etudeRegion)
  .dependsOn(etudeReligion)
  .dependsOn(etudeHttp)

lazy val etudeChatwork = project.in(file("libs/etude-chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(etudeHttp)
  .dependsOn(etudeQos)

lazy val etudeFile = project.in(file("libs/etude-file"))
  .settings(buildSettings: _*)

lazy val etudeHtml = project.in(file("libs/etude-html"))
  .settings(buildSettings: _*)

lazy val etudeHttp = project.in(file("libs/etude-http"))
  .settings(buildSettings: _*)
  .dependsOn(etudeIo)

lazy val etudeIo = project.in(file("libs/etude-io"))
  .settings(buildSettings: _*)

lazy val etudeMoney = project.in(file("libs/etude-money"))
  .settings(buildSettings: _*)

lazy val etudeQos = project.in(file("libs/etude-qos"))
  .settings(buildSettings: _*)

lazy val etudeRegion = project.in(file("libs/etude-region"))
  .settings(buildSettings: _*)

lazy val etudeReligion = project.in(file("libs/etude-religion"))
  .settings(buildSettings: _*)

lazy val etudeStenographer = project.in(file("libs/etude-stenographer"))
  .settings(buildSettings: _*)
  .dependsOn(etudeChatwork)
  .dependsOn(etudeFile)
