package etude.epice.utility.qos

import java.time.{Instant, Duration}
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock

case class TimeoutSemaphore(timeout: Duration) {
  private val semaphoreTimeOut: AtomicReference[Instant] = new AtomicReference[Instant]()

  private val semaphore: Semaphore = new Semaphore(1)

  private val operationLock = new ReentrantLock()

  def tryAcquire(): Boolean = {
    operationLock.lock()
    try {
      if (semaphore.tryAcquire()) {
        semaphoreTimeOut.set(Instant.now().plus(timeout))
        true
      } else {
        semaphoreTimeOut.get() match {
          case null => throw new IllegalStateException("Semaphore acquired without setting timeout")
          case t if t.isAfter(Instant.now()) =>
            semaphore.release() // Release semaphore by timeout
            true
          case _ =>
            false
        }
      }
    } finally {
      operationLock.unlock()
    }
  }

  def release(): Unit = {
    operationLock.lock()
    try {
      semaphore.release()
    } finally {
      operationLock.unlock()
    }
  }
}
