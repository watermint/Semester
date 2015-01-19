package etude.vino.chatwork.domain.model

import java.time.Instant

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ChunkSpec extends Specification {
  def chunk(low: BigInt, high: BigInt): Chunk = {
    Chunk(
      Instant.EPOCH,
      Instant.EPOCH,
      Instant.EPOCH,
      low,
      high
    )
  }

  "Chunk compaction" should {
    "single chunk" in {
      val a = chunk(1, 10)
      Chunk.compaction(Seq(a)) must equalTo(Seq(a))
    }

    "two mergeable chunks" in {
      val a = chunk(1, 10)
      val b = chunk(9, 20)
      val c = chunk(1, 20)
      Chunk.compaction(Seq(a, b)) must equalTo(Seq(c))
      Chunk.compaction(Seq(b, a)) must equalTo(Seq(c))
    }

    "two isolated chunks" in {
      val a = chunk(1, 10)
      val b = chunk(12, 20)
      Chunk.compaction(Seq(a, b)) must equalTo(Seq(a, b))
      Chunk.compaction(Seq(b, a)) must equalTo(Seq(a, b))
    }

    "more chunks" in {
      val a = chunk(1, 10)
      val b = chunk(9, 20)
      val c = chunk(22, 24)
      val d = chunk(30, 40)

      Chunk.compaction(Seq(a, b, c, d)) must equalTo(Seq(chunk(1, 20), chunk(22, 24), chunk(30, 40)))
    }

    "merge next" in {
      val a = chunk(1, 10)
      val b = chunk(9, 20)
      val c = chunk(21, 24)
      val d = chunk(30, 40)

      Chunk.compaction(Seq(a, b, c, d)) must equalTo(Seq(chunk(1, 24), chunk(30, 40)))
    }

    "all isolated" in {
      val a = chunk(1, 10)
      val b = chunk(12, 20)
      val c = chunk(23, 30)
      val d = chunk(38, 40)

      Chunk.compaction(Seq(a, b, c, d)) must equalTo(Seq(a, b, c, d))
    }
  }

  "Next chunk messageId" should {
    "provide message id from last chunk" in {
      val t = Instant.now
      val a = Chunk(t.minusSeconds(380), t.minusSeconds(300), t.minusSeconds(300), -1, 10)
      val b = Chunk(t.minusSeconds(280), t.minusSeconds(200), t.minusSeconds(200), 14, 15)

      Chunk.nextChunkMessageId(Seq(b), t.minusSeconds(290)) must beSome(14)
      Chunk.nextChunkMessageId(Seq(a, b), t.minusSeconds(290)) must beSome(14)
    }

    "no message id due to old" in {
      val t = Instant.now
      val a = Chunk(t.minusSeconds(380), t.minusSeconds(300), t.minusSeconds(300), -1, 10)
      val b = Chunk(t.minusSeconds(280), t.minusSeconds(200), t.minusSeconds(200), 14, 15)

      Chunk.nextChunkMessageId(Seq(b), t.minusSeconds(100)) must beNone
      Chunk.nextChunkMessageId(Seq(a, b), t.minusSeconds(100)) must beNone
    }

    "no message id due to epoch" in {
      val t = Instant.now
      val a = Chunk(t.minusSeconds(380), t.minusSeconds(300), t.minusSeconds(300), -1, 10)

      Chunk.nextChunkMessageId(Seq(a), t.minusSeconds(400)) must beNone
    }
  }
}
