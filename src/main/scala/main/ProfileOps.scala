package main

import javax.naming.directory.SearchResult

import cats.data.State
import cats.implicits._

object ProfileOps {

  import Profile._
  import ProfileFetcher.searchProfile

  private type Store = Map[Int, Profile]

  private def mkDN: String => String =
    s => s.split(":").tail.head.drop(1).replaceAll("""\\""", """\\\\""")

  private def modifyState: Int => (Store => Profile) => State[Store, Store] =
    k =>
      f =>
        State
          .modify[Store](st => st.updated(k, f(st)))
          .get

  private def getManager(k: String): State[Store, Option[Profile]] =
    for {
      res <- State.pure[Store, Option[SearchResult]](searchProfile(k))
      p <- res.fold[State[Store, Option[Profile]]](State.pure(None))(s =>
        createProfile(s).map(x => Option(x)))
    } yield p

  private def updateProfileWithManager(op: Option[Profile],
                                       pk: Int): State[Store, Store] = {
    op.fold(modifyState(pk)(st => st(pk).copy(manager = None)))(p =>
        modifyState(pk)(st =>
          st(pk)
            .copy(manager = Option(p))))
      .get
  }

  private def createProfile(s: SearchResult): State[Store, Profile] =
    for {
      attrs <- State.pure(s.getAttributes)
      dn <- State.pure(mkDN(attrs.get("distinguishedname").toString))
      key <- State.pure(dn.hashCode)
      maybeProfile <- State.inspect[Store, Option[Profile]](st => st.get(key))

      _ <- maybeProfile
        .fold(
          modifyState(key)(_ => profile(s.getAttributes))
        )(_ => State.get[Store])

      mgrKey = mkDN(attrs.get("manager").toString)

      maybeMgr <- State.inspect[Store, Option[Profile]](st =>
        st.get(mgrKey.hashCode))

      _ <- maybeMgr
        .fold(
          getManager(mgrKey)
            .flatMap(m => updateProfileWithManager(m, key))
        )(_ => updateProfileWithManager(maybeMgr, key))

      ret <- State.inspect[Store, Profile](st => st(key))
    } yield ret

  def buildProfiles: List[SearchResult] => List[Profile] = { xs =>
    xs.traverseU(s => createProfile(s)).runA(Map.empty).value
  }

}
