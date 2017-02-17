package main

import java.time.format.DateTimeFormatter
import java.time.{Period, ZonedDateTime}
import javax.naming.directory.Attributes

import cats.implicits._

import scala.language.postfixOps

final case class Profile(
    id: String,
    mail: String,
    department: Option[String] = None,
    streetAddress: Option[String] = None,
    designation: Option[String] = None,
    mobile: Option[String] = None,
    tel: Option[String] = None,
    country: Option[String] = None,
    postalCode: Option[String] = None,
    state: Option[String] = None,
    city: Option[String] = None,
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    manager: Option[Profile] = None,
    officeAddress: Option[String] = None,
    experience: Option[Int] = None
)

object Profile {

  private def toStr(implicit as: Attributes): String => Option[String] =
    i => Option(as.get(i)).map(_.get(0).toString)

  private def toPeriod: String => Period = { str =>
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.0X")
    val zdt = ZonedDateTime.parse(str, formatter)
    val today = ZonedDateTime.now(zdt.getZone).toLocalDate
    Period.between(zdt.toLocalDate, today)
  }

  def profile: Attributes => Profile = { implicit attribs =>
    def toStrF = toStr

    Profile(
      city = toStrF("l"),
      state = toStrF("st"),
      mail = toStrF("mail") getOrElse "",
      id = toStrF("samaccountname") getOrElse "",
      country = toStrF("co"),
      lastName = toStrF("sn"),
      firstName = toStrF("givenName"),
      department = toStrF("department"),
      designation = toStrF("description"),
      streetAddress = toStrF("streetAddress"),
      officeAddress = toStrF("phsicalDeliveryOfficeName"),
      mobile = toStrF("mobile"),
      tel = toStrF("telephoneNumber"),
      postalCode = toStrF("postalCode"),
      experience = toStrF("whenCreated").map(toPeriod).map(_.getYears),
      manager = None
    )
  }

  def unfold: Profile => List[Profile] =
    p => List(p) >>= (x => x :: x.manager.fold(List[Profile]())(unfold))
}
