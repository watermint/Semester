package etude.bootstrap

import scala.xml.{UnprefixedAttribute, Node, Elem}

object Html {
  /**
   * @see http://d.hatena.ne.jp/jmtaro/20100215/1266240012
   */
  def attr(kvs: (String, String)*) = new {
    def +:(elem: Elem) = elem % (Node.NoAttributes /: kvs) {
      case (attr, (k, v)) => new UnprefixedAttribute(k, v, attr)
    }
  }

  def a(href: String, v: Any): Elem = <a href={href}>{v}</a>
  def abbr(title: String, v: Any): Elem = <abbr title={title}>{v}</abbr>
  def address(v: Any): Elem = <address>{v}</address>
  def b(v: Any): Elem = <b>{v}</b>
  def blockquote(v: Any): Elem = <blockquote>{v}</blockquote>
  def br(): Elem = <br/>
  def button(v: Any): Elem = <button>{v}</button>
  def cite(v: Any): Elem = <cite>{v}</cite>
  def code(v: Any): Elem = <code>{v}</code>
  def dd(v: Any): Elem = <dd>{v}</dd>
  def div(v: Any): Elem = <div>{v}</div>
  def dl(v: Any): Elem = <dl>{v}</dl>
  def dt(v: Any): Elem = <dt>{v}</dt>
  def em(v: Any): Elem = <em>{v}</em>
  def form(v: Any): Elem = <form>{v}</form>
  def footer(v: Any): Elem = <footer>{v}</footer>
  def h1(v: Any): Elem = <h1>{v}</h1>
  def h2(v: Any): Elem = <h2>{v}</h2>
  def h3(v: Any): Elem = <h3>{v}</h3>
  def h4(v: Any): Elem = <h4>{v}</h4>
  def h5(v: Any): Elem = <h5>{v}</h5>
  def h6(v: Any): Elem = <h6>{v}</h6>
  def img(src: String, v: Any): Elem = <img src={src}/>
  def input(v: Any): Elem = <input>{v}</input>
  def label(v: Any): Elem = <label>{v}</label>
  def li(v: Any): Elem = <li>{v}</li>
  def nav(v: Any): Elem = <nav>{v}</nav>
  def ol(v: Any): Elem = <ol>{v}</ol>
  def option(v: Any): Elem = <option>{v}</option>
  def p(v: Any): Elem = <p>{v}</p>
  def pre(v: Any): Elem = <pre>{v}</pre>
  def select(v: Any): Elem = <select>{v}</select>
  def small(v: Any): Elem = <small>{v}</small>
  def span(v: Any): Elem = <span>{v}</span>
  def strong(v: Any): Elem = <strong>{v}</strong>
  def table(v: Any): Elem = <table>{v}</table>
  def tbody(v: Any): Elem = <tbody>{v}</tbody>
  def td(v: Any): Elem = <td>{v}</td>
  def thead(v: Any): Elem = <thead>{v}</thead>
  def tr(v: Any): Elem = <tr>{v}</tr>
  def ul(v: Any): Elem = <ul>{v}</ul>
}
