package main

import javax.naming.directory.Attributes

import scala.language.postfixOps

final case class Profile(
    id: String,
    mail: String,
    department: Option[String],
    streetAddress: Option[String],
    designation: Option[String],
    mobile: Option[String],
    tel: Option[String],
    country: Option[String],
    postalCode: Option[String],
    state: Option[String],
    city: Option[String],
    firstName: Option[String],
    lastName: Option[String],
    manager: Option[Profile],
    officeAddress: Option[String],
    experience: Option[String]
)

object Profile {

  private def toStr(implicit as: Attributes): String => Option[String] =
    i => Option(as.get(i)).map(_.get(0).toString)

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
      designation = toStrF("designation"),
      streetAddress = toStrF("streetAddress"),
      officeAddress = toStrF("phsicalDeliveryOfficeName"),
      mobile = toStrF("mobile"),
      tel = toStrF("telephoneNumber"),
      postalCode = toStrF("postalCode"),
      experience = toStrF("experience"),
      manager = None
    )
  }
}
