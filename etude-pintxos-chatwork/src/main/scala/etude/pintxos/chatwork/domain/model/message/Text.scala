package etude.pintxos.chatwork.domain.model.message

import java.time.Instant

import etude.domain.core.model.ValueObject
import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.text._
import etude.pintxos.chatwork.domain.model.room.RoomId

import scala.util.parsing.combinator.RegexParsers
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

  val horizontalRule: Parser[Fragment] = "[hr]" ^^ {
    hr => HorizontalRule()
  }

  val icon: Parser[Fragment] = "[picon:" ~> """\d+""".r <~ "]" ^^ {
    accountId => Icon(AccountId(accountId.toLong))
  }

  val iconWithName: Parser[Fragment] = "[piconname:" ~> """\d+""".r <~ "]" ^^ {
    accountId => IconWithName(AccountId(accountId.toLong))
  }

  val info: Parser[Fragment] = {
    "[info]" ~> opt("[title]" ~> (text | emptyText) <~ "[/title]") ~ rep(fragment) <~ "[/info]" ^^ {
      c => Info(c._1.map(_.text), Chunk(c._2))
    }
  }

  val quote: Parser[Fragment] = {
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

  val reply: Parser[Fragment] = {
    "[rp aid=" ~> """\d+""".r ~ " to=" ~ """\d+""".r ~ "-" ~ """\d+""".r <~ "]" ^^ {
      case aid ~ to ~ roomId ~ dash ~ messageId =>
        Reply(
          AccountId(aid.toLong),
          MessageId(RoomId(roomId.toLong), messageId.toLong)
        )
    }
  }

  val to: Parser[Fragment] = {
    "[To:" ~> """\d+""".r <~ "]" ^^ {
      aid =>
        To(AccountId(aid.toLong))
    }
  }

  val emptyText: Parser[PlainText] = "" ^^ { text => PlainText(text) }

  val text: Parser[PlainText] = new Parser[PlainText] {
    def snoop(in: Reader[Char], position: Int): Int = {
      if (in.atEnd) {
        // terminate at the end.
        position
      } else {
        if (endTag.apply(in).successful || tag.apply(in).successful) {
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

  val endTag: Parser[String] =
    "[/info]" |||
      "[/qt]" |||
      "[/title]"

  val tag: Parser[Fragment] = {
    horizontalRule |||
      icon |||
      iconWithName |||
      info |||
      quote |||
      reply |||
      to
  }

  val fragment: Parser[Fragment] = tag | text

  val chunk: Parser[Chunk] = rep(fragment) ^^ {
    fragments => Chunk(fragments)
  }

  def parse(text: String): ParseResult[Chunk] = parseAll(chunk, text)
}

