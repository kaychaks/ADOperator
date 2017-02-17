package main

import scala.util.{Failure, Success}

object Main extends App {

  import Conf._
  import Output._
  import ProfileFetcher._

  val group =
    String.format(conf.search.filterMemberOfTemplate, conf.search.group)

  {
    for {
      profiles <- getAllMembersInAGroup(group)
    } yield printUnfoldedProfilesForDD(conf.outputFileLoc)(profiles)
  } match {
    case Success(_) => println("Done")
    case Failure(e) => e.printStackTrace()
  }
}
