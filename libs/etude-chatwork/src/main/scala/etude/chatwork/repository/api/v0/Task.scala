package etude.chatwork.repository.api.v0

import java.time.Instant

case class Task(roomId: BigInt,
                taskId: BigInt,
                status: String,
                due: Instant,
                description: String,
                creatorAid: BigInt,
                assignedAid: BigInt)

object Task {
  def fromTaskDat(taskDat: Map[String, Any]): Task = {
    Task(
      roomId = taskDat.get("rid").get.asInstanceOf[BigInt],
      taskId = BigInt(taskDat.get("id").get.asInstanceOf[String]),
      status = taskDat.get("st").get.asInstanceOf[String],
      due = Instant.ofEpochSecond(taskDat.get("lt").get.asInstanceOf[BigInt].longValue()),
      description = taskDat.get("tn").get.asInstanceOf[String],
      creatorAid = BigInt(taskDat.get("bid").get.asInstanceOf[String]),
      assignedAid = BigInt(taskDat.get("aid").get.asInstanceOf[String])
    )
  }
}