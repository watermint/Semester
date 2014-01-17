
lazy val buildSettings = Seq(
  version := "0.0.21",
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
    "org.specs2" %% "specs2" % "2.3.7" % "test",
    "junit" % "junit" % "4.11" % "test"
  )
)


lazy val etudeChatworkCore = project.in(file("etude/chatwork/core"))
  .settings(buildSettings: _*)
  .dependsOn(etudeFoundationDomain)
  .dependsOn(etudeFoundationHttp)
  .dependsOn(etudeTextUndisclosed % "test")

lazy val etudeChatworkTeam = project.in(file("etude/chatwork/team"))
  .settings(buildSettings: _*)
  .dependsOn(etudeFoundationDomain)
  .dependsOn(etudeChatworkCore)
  .dependsOn(etudeChatworkElasticsearch)

lazy val etudeChatworkElasticsearch = project.in(file("etude/chatwork/elasticsearch"))
  .settings(buildSettings: _*)
  .dependsOn(etudeFoundationDomain)
  .dependsOn(etudeChatworkCore)
  .dependsOn(etudeElasticsearchCore)

lazy val etudeElasticsearchCore = project.in(file("etude/elasticsearch/core"))
  .settings(buildSettings: _*)
  .dependsOn(etudeFoundationUtility)

lazy val etudeFoundationDomain = project.in(file("etude/foundation/domain"))
  .settings(buildSettings: _*)

lazy val etudeFoundationCalendar = project.in(file("etude/foundation/calendar"))
  .settings(buildSettings: _*)
  .dependsOn(etudeFoundationDomain)
  .dependsOn(etudeFoundationHttp)
  .dependsOn(etudeFoundationI18n)

lazy val etudeFoundationHtml = project.in(file("etude/foundation/html"))
  .settings(buildSettings: _*)

lazy val etudeFoundationHttp = project.in(file("etude/foundation/http"))
  .settings(buildSettings: _*)
  .dependsOn(etudeFoundationUtility)

lazy val etudeFoundationI18n = project.in(file("etude/foundation/i18n"))
  .settings(buildSettings: _*)

lazy val etudeFoundationUtility = project.in(file("etude/foundation/utility"))
  .settings(buildSettings: _*)

lazy val etudeDesktopFextile = project.in(file("etude/desktop/fextile"))
  .settings(buildSettings: _*)

lazy val etudeTextUndisclosed = project.in(file("etude/test/undisclosed"))
  .settings(buildSettings: _*)

