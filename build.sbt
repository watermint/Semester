
lazy val buildSettings = Seq(
  version := "0.13.7",
  organization := "org.watermint",
  scalaVersion := "2.11.4",
  crossScalaVersions := Seq("2.11.4"),
  resolvers ++= Seq(
    "sonatype-public" at "https://oss.sonatype.org/content/groups/public",
    "atlassian-public" at "https://maven.atlassian.com/repository/public",
    "maven-central" at "http://repo1.maven.org/maven2",
    "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "org.specs2" %% "specs2-core" % "latest.release" % "test",
    "org.specs2" %% "specs2-junit" % "latest.release" % "test",
    "junit" % "junit" % "latest.release" % "test"
  ),
  ivyXML :=
    <dependencies>
      <exclude org="log4j" name="log4j" />
      <exclude org="commons-logging" name="commons-logging" />
      <exclude org="org.slf4j" name="slf4j-log4j12" />
    </dependencies>
)

// ---- table

lazy val tableChitarra = project.in(file("etude-table-chitarra"))
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)

// ---- pintxos
lazy val pintxosPocket = project.in(file("etude-pintxos-pocket"))
  .dependsOn(manieresDomain)
  .dependsOn(epiceLogging)
  .dependsOn(epiceHttp)
  .dependsOn(gazpachoSpray)
  .settings(buildSettings: _*)

lazy val pintxosNsunc = project.in(file("etude-pintxos-nsunc"))
  .dependsOn(painFoundation)
  .settings(buildSettings: _*)

lazy val pintxosThings = project.in(file("etude-pintxos-things"))
  .settings(buildSettings: _*)

lazy val pintxosChatwork = project.in(file("etude-pintxos-chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(manieresDomain)
  .dependsOn(manieresEvent)
  .dependsOn(painRangement)
  .dependsOn(epiceHttp)
  .dependsOn(epiceUtility)
  .dependsOn(epiceLogging)
  .dependsOn(epiceUndisclosed % "test")


// ---- manieres
lazy val manieresDomain = project.in(file("etude-manieres-domain"))
  .settings(buildSettings: _*)

lazy val manieresEvent = project.in(file("etude-manieres-event"))
  .settings(buildSettings: _*)
  .dependsOn(manieresDomain)

// ---- gazpacho
lazy val gazpachoSpray = project.in(file("etude-gazpacho-spray"))
  .settings(buildSettings: _*)

// ---- pain
lazy val painFoundation = project.in(file("etude-pain-foundation"))
  .settings(buildSettings: _*)
  .dependsOn(epiceLogging)

lazy val painRangement = project.in(file("etude-pain-rangement"))
  .settings(buildSettings: _*)

lazy val painHighlight = project.in(file("etude-pain-highlight"))
  .settings(buildSettings: _*)

lazy val painTika = project.in(file("etude-pain-tika"))
  .settings(buildSettings: _*)

// ---- epice
lazy val epiceLogging = project.in(file("etude-epice-logging"))
  .settings(buildSettings: _*)

lazy val epiceHttp = project.in(file("etude-epice-http"))
  .settings(buildSettings: _*)
  .dependsOn(epiceUtility)
  .dependsOn(epiceLogging)

lazy val epiceUtility = project.in(file("etude-epice-utility"))
  .settings(buildSettings: _*)

lazy val epiceUndisclosed = project.in(file("etude-epice-undisclosed"))
  .settings(buildSettings: _*)
  .dependsOn(epiceLogging)


// ---- kitchenette
lazy val vinoChatwork = project.in(file("etude-vino-chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(pintxosChatwork)
  .dependsOn(pintxosNsunc)
  .settings(assemblySettings: _*)

