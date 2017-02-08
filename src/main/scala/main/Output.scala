package main

import java.nio.file.{Files, Path}

import cats.implicits._

import scala.util.Try

object Output {
  import ShowInstances._
  def printProfiles: Path => List[Profile] => Try[Unit] = { file => ps =>
    for {
      _ <- Try {
        Files.write(file, ps.show.getBytes)
      }
    } yield ()
  }
}
