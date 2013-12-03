package punchedtape

import etude.chatwork.v0.Session

case class Tape(punches: List[Punch], infinite: Boolean) {
  def play(session: Session): Unit = {
    if (infinite) {
      while (true) {
        punches.foreach(_.execute(session))
        Thread.sleep(30 * 1000)
      }
    } else {
      punches.foreach(_.execute(session))
    }
  }
}
