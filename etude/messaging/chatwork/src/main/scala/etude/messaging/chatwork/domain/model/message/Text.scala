package etude.messaging.chatwork.domain.model.message

import java.time.Instant
import scala.util.parsing.combinator.RegexParsers
import etude.foundation.domain.model.ValueObject
import etude.messaging.chatwork.domain.model.message.text._
import etude.messaging.chatwork.domain.model.account.AccountId
import etude.messaging.chatwork.domain.model.room.RoomId
import scala.util.parsing.input.Reader

case class Text(text: String)
  extends ValueObject {

  lazy val chunk: Chunk = {
    try {
      Text.parse(text).get
    } catch {
      case _: Throwable =>
        // fall back to plain text
        Chunk(Seq(PlainText(text)))
    }
  }

  lazy val fragments: Seq[Fragment] = chunk.fragments()

  lazy val to: Seq[AccountId] = fragments.collect {
    case f: To => f.accountId
  }

  lazy val replyTo: Seq[AccountId] = fragments.collect {
    case f: Reply => f.accountId
  }
}

object Text extends RegexParsers {
  def apply(fragment: Fragment): Text = Text(fragment.render())

  override def skipWhitespace = false

  def horizontalRule: Parser[Fragment] = "[hr]" ^^ {
    hr => HorizontalRule()
  }

  def icon: Parser[Fragment] = "[picon:" ~> """\d+""".r <~ "]" ^^ {
    accountId => Icon(AccountId(accountId.toLong))
  }

  def iconWithName: Parser[Fragment] = "[piconname:" ~> """\d+""".r <~ "]" ^^ {
    accountId => IconWithName(AccountId(accountId.toLong))
  }

  def info: Parser[Fragment] = {
    "[info]" ~> opt("[title]" ~> text <~ "[/title]") ~ rep(fragment) <~ "[/info]" ^^ {
      c => Info(c._1.map(_.text), Chunk(c._2))
    }
  }

  def quote: Parser[Fragment] = {
    "[qt][qtmeta aid=" ~> """\d+""".r ~
      opt(" time=" ~> """\d+""".r) ~ "]" ~ rep(fragment) <~ "[/qt]" ^^ {
      case aid ~ time ~ bracket ~ fragment =>
        Quote(
          AccountId(aid.toLong),
          time.map {
            t => Instant.ofEpochSecond(t.toLong)
          },
          Chunk(fragment))
    }
  }

  def reply: Parser[Fragment] = {
    "[rp aid=" ~> """\d+""".r ~ " to=" ~ """\d+""".r ~ "-" ~ """\d+""".r <~ "]" ^^ {
      case aid ~ to ~ roomId ~ dash ~ messageId =>
        Reply(
          AccountId(aid.toLong),
          MessageId(RoomId(roomId.toLong), messageId.toLong)
        )
    }
  }

  def to: Parser[Fragment] = {
    "[To:" ~> """\d+""".r <~ "]" ^^ {
      aid =>
        To(AccountId(aid.toLong))
    }
  }

  def text: Parser[PlainText] = new Parser[PlainText] {
    def snoop(in: Reader[Char], position: Int): Int = {
      if (in.atEnd) {
        // terminate at the end.
        position
      } else {
        if (tag.apply(in).successful || endTag.apply(in).successful) {
          // terminate when tag or end tag found.
          position
        } else {
          // proceed next
          snoop(in.rest, position + 1)
        }
      }
    }

    def apply(in: Reader[Char]): Text.ParseResult[PlainText] = {
      val position = snoop(in, in.offset)

      if (position == in.offset) {
        Failure(
          msg = "No plain text found",
          next = in
        )
      } else {
        Success(
          result = PlainText(in.source.subSequence(in.offset, position).toString),
          next = in.drop(position - in.offset)
        )
      }
    }
  }

  def endTag: Parser[String] =
    "[/info]" |||
      "[/qt]" |||
      "[/title]"

  def tag: Parser[Fragment] = {
    horizontalRule |||
      icon |||
      iconWithName |||
      info |||
      quote |||
      reply |||
      to
  }

  def fragment: Parser[Fragment] = tag | text

  def chunk: Parser[Chunk] = rep(fragment) ^^ {
    fragments => Chunk(fragments)
  }

  def parse(text: String): ParseResult[Chunk] = parseAll(chunk, text)
}

