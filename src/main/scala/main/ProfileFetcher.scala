package main

import javax.naming.directory._

import scala.collection.JavaConverters._
import scala.util.Try

object ProfileFetcher {
  import Conf._
  import ProfileOps.buildProfiles

  private def search(implicit ctx: InitialDirContext,
             base: String,
             ctrl: SearchControls): String => List[SearchResult] = { filter =>
    ctx.search(base, filter, ctrl).asScala.toList
  }

  private def searchF: (String) => List[SearchResult] = search

  private def getMemberShips: List[SearchResult] => List[String] =
    xs =>
      for {
        res <- xs
        m <- res.getAttributes
          .get("memberOf")
          .getAll
          .asScala
          .toList
          .map(_.toString)
      } yield m

  def searchProfile: String => Option[SearchResult] = { dn =>
    val filter = String.format(conf.search.filterUserTemplate, dn)
    searchF(filter).headOption
  }

  def getAllMembersInAGroup: (String) => Try[List[Profile]] =
    g =>
      for {
        members <- Try { searchF(g) }
        profiles <- Try { buildProfiles(members) }
      } yield profiles

}
