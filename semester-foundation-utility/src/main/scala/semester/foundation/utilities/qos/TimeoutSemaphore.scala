package semester.foundation.utilities.qos

import java.time.{Duration, Instant}
import java.util.concurrent.Semaphore
import java.util.concurrent.locks.ReentrantLock

import semester.foundation.utilities.atomic.Reference

case class TimeoutSemaphore(timeout: Duration) {
  private val semaphoreTimeOut: Reference[Instant] = new Reference[Instant]()

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
          case None => throw new IllegalStateException("Semaphore acquired without setting timeout")
          case Some(t) =>
            if (t.isAfter(Instant.now())) {
              semaphore.release() // Release semaphore by timeout
              true
            } else {
              false
            }
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
