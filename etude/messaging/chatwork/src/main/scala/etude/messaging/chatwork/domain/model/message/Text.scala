package etude.messaging.chatwork.domain.model.message

import scala.util.parsing.combinator.RegexParsers
import etude.foundation.domain.model.ValueObject
import etude.messaging.chatwork.domain.model.message.text._
import etude.messaging.chatwork.domain.model.message.text.HorizontalRule
import etude.messaging.chatwork.domain.model.message.text.Icon
import etude.messaging.chatwork.domain.model.message.text.Chunk
import etude.messaging.chatwork.domain.model.account.AccountId
import java.time.Instant
import etude.messaging.chatwork.domain.model.room.RoomId

case class Text(text: String)
  extends ValueObject

object Text extends RegexParsers {
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

  def text: Parser[PlainText] = """[^\[]+""".r ^^ {
    text => PlainText(text)
  }

  def fragment: Parser[Fragment] = {
    (horizontalRule |||
      icon |||
      iconWithName |||
      info |||
      quote |||
      reply |||
      to) |
      text
  }

  def chunk: Parser[Fragment] = rep(fragment) ^^ {
    fragments => Chunk(fragments)
  }

  def parse(text: String) = parseAll(chunk, text)
}

