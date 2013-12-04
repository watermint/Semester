package stenographer

import com.twitter.finatra.FinatraServer
import stenographer.views.{Admin, Root, Assets}
import etude.bootstrap._
import etude.bootstrap.SiteConfig
import scala.Some
import etude.file.{Dir, Home}
import java.util.UUID

object App extends FinatraServer {
  // This is Semester project structure specific configuration.
  System.setProperty("com.twitter.finatra.config.docRoot", "apps/stenographer/src/main/resources")

  register(new Assets())
  register(new Admin())
  register(new Root())

  lazy val bootId: String = UUID.randomUUID().toString

  lazy val home: Dir = Dir(Home.user.javaPath.resolve(".stenographer")) // TODO: refactor Dir

  lazy val elasticSearchHome: Dir = Dir(home.javaPath.resolve("elasticsearch"))

  lazy val siteConfig = SiteConfig(
    "/assets",
    "/assets/js/jquery-2.0.3.min.js"
  )

  lazy val adminSiteConfig = siteConfig.copy(bodyClass = Some("admin"), scripts = List("/assets/js/stenographer_admin.js"))

  lazy val page = Page(siteConfig)

  lazy val adminPage = Page(adminSiteConfig)

  lazy val headers = List(
    <link rel="stylesheet" href="/assets/css/stenographer.css"/>,
    <link rel="shortcut icon" id="favicon" type="image/png" href="/assets/imgs/favicon.png"/>
  )

  lazy val footers = Html.footer(
    List(
      Text.muted(
        List(
          Html.a("https://github.com/watermint/Semester/tree/master/apps/stenographer", "Stenographer "),
          Icons.copyrightMark,
          " Takayuki Okazaki"
        )
      )
    )
  )

  def contents(v: Any): Any = Div.container(
    List(
      Div.pageHeader(<img id="stenographer-logo" src="/assets/imgs/logo@2x.png" alt="Stenographer"/>),
      v,
      Div.clearfix,
      footers
    )
  )

  def html(title: String, v: Any): String = page.html(title, headers, contents(v)).toString()

  def adminHtml(title: String, v: Any): String = adminPage.html(title, headers, contents(v)).toString()
}
