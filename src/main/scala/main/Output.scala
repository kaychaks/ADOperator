package main

import java.nio.file.{Files, Path}

import cats.implicits._
import main.ShowInstances.StdProfile._

import scala.util.Try
object Output {

  import Profile._
  type PrintOutput = Path => List[Profile] => Try[Unit]

  def printProfiles: PrintOutput = { file => ps =>
    Try {
      Files.write(file, ps.show.getBytes)
    }
  }

  def printUnfoldedProfiles: PrintOutput = { file => ps =>
    Try {
      val dp = (ps >>= unfold).distinct
      Files.write(file, dp.show.getBytes)
    }
  }

  def printUnfoldedProfilesWithManagerId: PrintOutput = { file => ps =>
    Try {
      import main.ShowInstances.AltProfile._
      val dp = (ps >>= unfold).distinct
      Files.write(file, dp.show.getBytes)
    }
  }
}
