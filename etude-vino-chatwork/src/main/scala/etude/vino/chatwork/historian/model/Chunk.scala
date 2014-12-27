package etude.vino.chatwork.historian.model

import java.time.Instant

import etude.pintxos.chatwork.domain.model.message.Message
import org.json4s.JsonDSL._
import org.json4s._

import scala.collection.mutable.ArrayBuffer

case class Chunk(roomId: BigInt,
                 lowTime: Instant,
                 highTime: Instant,
                 low: BigInt,
                 high: BigInt) extends Entity {
  def toJSON: JValue = {
    ("roomId" -> roomId) ~
      ("lowTime" -> lowTime.toString) ~
      ("highTime" -> highTime.toString) ~
      ("low" -> low) ~
      ("high" -> high)
  }

  val isEpoch: Boolean = {
    low < 0
  }
}

object Chunk extends Parser[Chunk] {
  /**
   * a.low < b.low
   *
   * @return merged chunk or 'a'
   */
  private def merge(a: Chunk, b: Chunk): Either[Chunk, Chunk] = {
    if (a.low <= b.low && b.low <= a.high) {
      Left(
        Chunk(
          a.roomId,
          a.lowTime,
          b.highTime,
          a.low,
          b.high
        )
      )
    } else if (b.low - a.high == BigInt(1)) {
      Left(
        Chunk(
          a.roomId,
          a.lowTime,
          b.highTime,
          a.low,
          b.high
        )
      )
    } else {
      Right(a)
    }
  }

  private def compactionImpl(chunks: Seq[Chunk]): Seq[Chunk] = {
    val results = ArrayBuffer[Chunk]()
    var p = 0
    var q = 1
    var a = chunks(p)

    while (p < chunks.size && q < chunks.size) {
      merge(a, chunks(q)) match {
        case Left(c) =>
          a = c
          q += 1
        case Right(c) =>
          results += c
          a = chunks(q)
          p += 1
      }
    }
    results += a

    results.seq
  }

  def compaction(chunks: Seq[Chunk]): Seq[Chunk] = {
    compactionImpl(chunks.sortBy(_.low))
  }

  def ofChunks(json: JValue): Seq[Chunk] = {
    for {
      JObject(o) <- json
      JField("roomId", JInt(roomId)) <- o
      JField("lowTime", JString(lowTime)) <- o
      JField("highTime", JString(highTime)) <- o
      JField("low", JInt(low)) <- o
      JField("high", JInt(high)) <- o
    } yield {
      Chunk(
        roomId,
        Instant.parse(lowTime),
        Instant.parse(highTime),
        low,
        high
      )
    }
  }

  def fromJSON(json: JValue): Chunk = {
    ofChunks(json).last
  }

  def fromMessages(messages: Seq[Message]): Chunk = {
    val m = messages.sortBy(_.messageId.messageId)
    val first = m(0)
    val last = m.last

    Chunk(
      first.messageId.roomId.value,
      first.ctime,
      last.ctime,
      first.messageId.messageId,
      last.messageId.messageId
    )
  }

  def nextChunkMessageId(chunks: Seq[Chunk], timeAfter: Instant): Option[BigInt] = {
    val c = chunks.maxBy(_.low)
    if (c.isEpoch) {
      None
    } else if (c.lowTime.isAfter(timeAfter)) {
      Some(c.low)
    } else {
      None
    }
  }

}