package punchedtape

import etude.chatwork.Session

case class Tape(punches: List[Punch]) {
  def play(session: Session): Unit = {
    punches.foreach(_.execute(session))
  }
}
