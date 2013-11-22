package punchedtape

import etude.chatwork.Session

trait Punch {
  def execute(session: Session): Boolean
}


