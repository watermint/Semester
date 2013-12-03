package punchedtape

import etude.chatwork.v0.Session

trait Punch {
  def execute(session: Session): Boolean
}


