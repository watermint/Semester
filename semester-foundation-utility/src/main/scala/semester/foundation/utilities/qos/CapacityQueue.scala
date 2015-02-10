package semester.foundation.utilities.qos

import java.util.concurrent.locks.ReentrantLock

import scala.collection.mutable.ArrayBuffer

class CapacityQueue[T](val capacity: Int) {
  private val operationLock = new ReentrantLock()

  private val queue = new ArrayBuffer[T]()

  def enqueue(v: T): Unit = {
    operationLock.lock()
    try {
      if (queue.size > capacity) {
        queue.remove(0)
      }
      queue += v
    } finally {
      operationLock.unlock()
    }
  }

  def dequeue(): Option[T] = {
    operationLock.lock()
    try {
      if (queue.size > 0) {
        Some(queue.remove(0))
      } else {
        None
      }
    } finally {
      operationLock.unlock()
    }
  }

  def toSeq: Seq[T] = queue.toSeq
}
