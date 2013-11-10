package stenographer

import scala.xml.Elem

object View {
  def skeleton(title: String, content: Any, bodyClazz: Option[String] = None): String = {
    html(
      List(
        head(
          List(
            <meta charset="UTF-8"/>,
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>,
            <title>{title}</title>,
            <link rel="stylesheet" href="/assets/css/bootstrap.min.css"/>,
            <link rel="stylesheet" href="/assets/css/stenographer.css"/>
          )
        ),
        body(
          List(
            content,
            <script src="/assets/js/jquery-2.0.3.min.js"></script>,
            <script src="/assets/js/bootstrap.min.js"></script>
          ),
          bodyClazz
        )
      )
    ).toString()
  }

  def html(content: Any): Elem = <html>{content}</html>

  def head(content: Any): Elem = <head>{content}</head>

  def body(content: Any, clazz: Option[String] = None): Elem = {
    clazz match {
      case Some(c) => <body class={c}>{content}</body>
      case _ => <body>{content}</body>
    }
  }

  def divContainer(content: Any, clazz: Option[String] = None): Elem = {
    clazz match {
      case Some(c) => <div class={"container " + c}>{content}</div>
      case _ => <div class="container">{content}</div>
    }
  }
}
