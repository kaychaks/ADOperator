package main

import cats.Show
import cats.implicits.{catsStdShowForString => _, _}

object ShowInstances {
  private object CommonShowInstances {
    implicit val tsString: Show[String] = (f: String) => s""""$f""""

    implicit val tsOpt: Show[Option[String]] = {
      case Some(s) => s.show
      case None => s"""null\t"""
    }
  }

  object StdProfile {
    import CommonShowInstances._

    implicit val tsListStr: Show[List[Profile]] = {
      case x :: xs => s"${x.show}\n${xs.show}"
      case Nil => ""
    }

    implicit val tsOptPro: Show[Option[Profile]] = {
      case Some(p) => p.show
      case None => s"""null\t"""
    }

    implicit val showProfile: Show[Profile] = { (f: Profile) =>
      s"""
${f.id.show}\t
${f.firstName.show}\t
${f.lastName.show}\t
${f.mail.show}\t
${f.department.show}\t
${f.streetAddress.show}\t
${f.designation.show}\t
${f.mobile.show}\t
${f.tel.show}\t
${f.country.show}\t
${f.postalCode.show}\t
${f.state.show}\t
${f.city.show}\t
${f.officeAddress.show}\t
${f.experience.show}\t
[${f.manager.show}]
      """.stripMargin.replaceAll("\n", "")
    }
  }

  object AltProfile {

    import CommonShowInstances._

    implicit val tsListStr: Show[List[Profile]] = {
      case x :: xs => s"${x.show}\n${xs.show}"
      case Nil => ""
    }

    implicit val tsOptProOnlyId: Show[Option[Profile]] = {
      case Some(p) => p.id.show
      case None => s"""null\t"""
    }

    implicit val showProfileWithManagerIds: Show[Profile] = { (f: Profile) =>
      s"""
${f.id.show}\t
${f.firstName.show}\t
${f.lastName.show}\t
${f.mail.show}\t
${f.department.show}\t
${f.streetAddress.show}\t
${f.designation.show}\t
${f.mobile.show}\t
${f.tel.show}\t
${f.country.show}\t
${f.postalCode.show}\t
${f.state.show}\t
${f.city.show}\t
${f.officeAddress.show}\t
${f.experience.show}\t
${f.manager.show}
      """.stripMargin.replaceAll("\n", "")
    }
  }

}
