
lazy val buildSettings = Seq(
  version := "0.14.4",
  organization := "org.watermint",
  scalaVersion := "2.11.5",
  crossScalaVersions := Seq("2.11.5"),
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

//
// foundation libraries
// 

lazy val foundationDomain = project.in(file("semester-foundation-domain"))
  .settings(buildSettings: _*)

lazy val foundationUtility = project.in(file("semester-foundation-utility"))
  .settings(buildSettings: _*)
  .dependsOn(foundationLogging)

lazy val foundationLogging = project.in(file("semester-foundation-logging"))
  .settings(buildSettings: _*)

lazy val foundationHttp = project.in(file("semester-foundation-http"))
  .settings(buildSettings: _*)
  .dependsOn(foundationUtility)
  .dependsOn(foundationLogging)

lazy val foundationUndisclosed = project.in(file("semester-foundation-undisclosed"))
  .settings(buildSettings: _*)
  .dependsOn(foundationLogging)


//
// wrapper api for external services
//

lazy val servicePocket = project.in(file("semester-service-pocket"))
  .dependsOn(foundationDomain)
  .dependsOn(foundationLogging)
  .dependsOn(foundationHttp)
  .dependsOn(readymadeSpray)
  .settings(buildSettings: _*)

lazy val serviceNsunc = project.in(file("semester-service-nsunc"))
  .dependsOn(readymadeCf)
  .settings(buildSettings: _*)

lazy val serviceThings = project.in(file("semester-service-things"))
  .settings(buildSettings: _*)

lazy val serviceChatwork = project.in(file("semester-service-chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(foundationDomain)
  .dependsOn(foundationHttp)
  .dependsOn(foundationUtility)
  .dependsOn(foundationLogging)
  .dependsOn(foundationUndisclosed % "test")


//
// ready made wrapper api/configuration for libraries
//

lazy val readymadeSpray = project.in(file("semester-readymade-spray"))
  .settings(buildSettings: _*)

lazy val readymadeHtml = project.in(file("semester-readymade-html"))
  .settings(buildSettings: _*)

lazy val readymadeHighlight = project.in(file("semester-readymade-highlight"))
  .settings(buildSettings: _*)

lazy val readymadeTika = project.in(file("semester-readymade-tika"))
  .settings(buildSettings: _*)

lazy val readymadeCf = project.in(file("semester-readymade-cf"))
  .settings(buildSettings: _*)


//
// applications
//

lazy val applicationVino = project.in(file("semester-application-vino"))
  .settings(buildSettings: _*)
  .dependsOn(serviceChatwork)
  .dependsOn(serviceNsunc)
  .settings(assemblySettings: _*)

lazy val applicationChitarra = project.in(file("semester-application-chitarra"))
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)

