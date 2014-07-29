
lazy val buildSettings = Seq(
  version := "0.7.12",
  organization := "org.watermint",
  scalaVersion := "2.11.2",
  crossScalaVersions := Seq("2.11.2"),
  resolvers ++= Seq(
    "sonatype-public" at "https://oss.sonatype.org/content/groups/public",
    "atlassian-public" at "https://maven.atlassian.com/repository/public",
    "maven-central" at "http://repo1.maven.org/maven2",
    "twitter-repo" at "http://maven.twttr.com",
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "latest.integration" % "test",
    "junit" % "junit" % "latest.integration" % "test"
  ),
  ivyXML :=
    <dependencies>
      <exclude org="log4j" name="log4j" />
      <exclude org="commons-logging" name="commons-logging" />
      <exclude org="org.slf4j" name="slf4j-log4j12" />
    </dependencies>
)

// ---- table

lazy val tableArrabbiata = project.in(file("etude-table-arrabbiata"))
  .dependsOn(pintxosChatwork)
  .dependsOn(pintxosThings)
  .dependsOn(epiceLogging)
  .dependsOn(vinoChatwork)
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)

lazy val tableBolognese = project.in(file("etude-table-bolognese"))
  .dependsOn(epiceLogging)
  .dependsOn(gazpachoSpray)
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)


// ---- pintxos
lazy val pintxosPocket = project.in(file("etude-pintxos-pocket"))
  .dependsOn(manieresDomain)
  .dependsOn(epiceLogging)
  .dependsOn(epiceHttp)
  .dependsOn(gazpachoSpray)
  .settings(buildSettings: _*)

lazy val pintxosThings = project.in(file("etude-pintxos-things"))
  .settings(buildSettings: _*)

lazy val pintxosFlickr = project.in(file("etude-pintxos-flickr"))
  .dependsOn(manieresDomain)
  .dependsOn(epiceUtility)
  .dependsOn(epiceLogging)
  .settings(buildSettings: _*)

lazy val pintxosChatwork = project.in(file("etude-pintxos-chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(manieresDomain)
  .dependsOn(painRangement)
  .dependsOn(epiceHttp)
  .dependsOn(epiceUtility)
  .dependsOn(epiceLogging)
  .dependsOn(epiceUndisclosed % "test")


// ---- manieres
lazy val manieresDomain = project.in(file("etude-manieres-domain"))
  .settings(buildSettings: _*)


// ---- gazpacho
lazy val gazpachoFextile = project.in(file("etude-gazpacho-fextile"))
  .settings(buildSettings: _*)
  .dependsOn(epiceLogging)

lazy val gazpachoSpray = project.in(file("etude-gazpacho-spray"))
  .settings(buildSettings: _*)

// ---- pain
lazy val painRangement = project.in(file("etude-pain-rangement"))
  .settings(buildSettings: _*)

lazy val painHighlight = project.in(file("etude-pain-highlight"))
  .settings(buildSettings: _*)

lazy val painElasticsearch = project.in(file("etude-pain-elasticsearch"))
  .settings(buildSettings: _*)
  .dependsOn(manieresDomain)
  .dependsOn(epiceLogging)

lazy val painTika = project.in(file("etude-pain-tika"))
  .settings(buildSettings: _*)

// ---- epice
lazy val epiceLogging = project.in(file("etude-epice-logging"))
  .settings(buildSettings: _*)

lazy val epiceSel = project.in(file("etude-epice-sel"))
  .settings(buildSettings: _*)
  .dependsOn(epiceLogging)

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
  .settings(assemblySettings: _*)

lazy val vinoCode = project.in(file("etude-vino-code"))
  .settings(buildSettings: _*)
  .dependsOn(manieresDomain)
  .dependsOn(painTika)
  .dependsOn(painHighlight)
  .dependsOn(painElasticsearch)

