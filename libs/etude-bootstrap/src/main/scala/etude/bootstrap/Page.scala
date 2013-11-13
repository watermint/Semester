package etude.bootstrap

import scala.xml.Elem

case class Page(config: SiteConfig) {
  def html(title: String, headers: Any, contents: Any): Elem =
    <html>{head(title, headers)}{body(contents)}</html>

  def head(title: String, headers: Any): Elem =
    <head>{basicHeaders}<title>{title}</title>{headers}{bootstrapCss}</head>

  def body(contents: Any): Elem = {
    config.bodyClass match {
      case Some(bc) => <body class={bc}>{contents}{bootstrapScripts}</body>
      case _ => <body>{contents}{bootstrapScripts}</body>
    }
  }

  lazy val basicHeaders: List[Elem] = List(
    <meta charset="UTF-8"/>,
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  )

  lazy val bootstrapCss: List[Elem] = List(
    <link rel="stylesheet" href={config.bootstrapBasePath + "/css/bootstrap.min.css"}/>
  )

  lazy val bootstrapScripts: List[Elem] = List(
    <script src={config.jqueryPath}></script>,
    <script src={config.bootstrapBasePath + "/js/bootstrap.min.js"}></script>
  ) ++ config.scripts.map(s => <script src={s}></script>)
}

object Page {
  def apply(): Page = {
    Page(
      SiteConfig(
        bootstrapBasePath = "//netdna.bootstrapcdn.com/bootstrap/3.0.2/",
        jqueryPath = "//code.jquery.com/jquery-2.0.0.min.js"
      )
    )
  }
}