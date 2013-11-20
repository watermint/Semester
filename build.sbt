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
  .dependsOn(libsEtudeMoney)
  .dependsOn(libsEtudeHtml)

lazy val appsStenographer = project.in(file("apps/stenographer"))
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)
  .dependsOn(libsEtudeBootstrap)
  .dependsOn(libsEtudeChatwork)
  .dependsOn(libsEtudeFile)
  .dependsOn(libsEtudeStenographer)

lazy val libsEtudeAggregation = project.in(file("libs/etude-aggregation"))
  .settings(buildSettings: _*)

lazy val libsEtudeBootstrap = project.in(file("libs/etude-bootstrap"))
  .settings(buildSettings: _*)

lazy val libsEtudeCalendar = project.in(file("libs/etude-calendar"))
  .settings(buildSettings: _*)
  .dependsOn(libsEtudeAggregation)
  .dependsOn(libsEtudeRegion)
  .dependsOn(libsEtudeReligion)
  .dependsOn(libsEtudeHttp)

lazy val libsEtudeChatwork = project.in(file("libs/etude-chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(libsEtudeHttp)
  .dependsOn(libsEtudeQos)

lazy val libsEtudeFile = project.in(file("libs/etude-file"))
  .settings(buildSettings: _*)

lazy val libsEtudeHtml = project.in(file("libs/etude-html"))
  .settings(buildSettings: _*)

lazy val libsEtudeHttp = project.in(file("libs/etude-http"))
  .settings(buildSettings: _*)
  .dependsOn(libsEtudeIo)

lazy val libsEtudeIo = project.in(file("libs/etude-io"))
  .settings(buildSettings: _*)

lazy val libsEtudeMoney = project.in(file("libs/etude-money"))
  .settings(buildSettings: _*)

lazy val libsEtudeQos = project.in(file("libs/etude-qos"))
  .settings(buildSettings: _*)

lazy val libsEtudeRegion = project.in(file("libs/etude-region"))
  .settings(buildSettings: _*)

lazy val libsEtudeReligion = project.in(file("libs/etude-religion"))
  .settings(buildSettings: _*)

lazy val libsEtudeStenographer = project.in(file("libs/etude-stenographer"))
  .settings(buildSettings: _*)
  .dependsOn(libsEtudeChatwork)
  .dependsOn(libsEtudeFile)
