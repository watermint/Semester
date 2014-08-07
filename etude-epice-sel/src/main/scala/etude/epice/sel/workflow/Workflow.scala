package etude.epice.sel.workflow

import java.net.URI

import etude.epice.sel.auth.oauth.OAuthAccessToken

object Workflow {

  trait Stage {
    val name: String
  }

  case class StagePlain() extends Stage {
    val name: String = "Plain"
  }

  case class StageAuthorized() extends Stage {
    val name: String = "Authorized"
  }

  case class StageSpecial() extends Stage {
    val name: String = "Special"
  }

  case class Transition[FS <: Stage, TS <: Stage](from: FS, to: TS)

  def main (args: Array[String]) {
    val plainToAuthorized: PartialFunction[(Stage, Stage), Stage] = {
      case (f: StagePlain, t: StageAuthorized) =>
        println(s"${f.name} -> ${t.name}")
        t
    }
    val authorizedToSpecial: PartialFunction[(Stage, Stage), Stage] = {
      case (f: StageAuthorized, t: StageSpecial) =>
        println(s"${f.name} -> ${t.name}")
        t
    }

    val workflow = List[PartialFunction[(Stage, Stage), Stage]](
      plainToAuthorized,
      authorizedToSpecial
    )

    val stagePlain = StagePlain()
    val stageAuthorized = StageAuthorized()
    val stageSpecial = StageSpecial()

    val prepare: PartialFunction[Transition, String] = {
      case t: Transition[StagePlain, StageAuthorized] =>
        s"${t.from.name} -> ${t.to.name}"
    }

  }
}
