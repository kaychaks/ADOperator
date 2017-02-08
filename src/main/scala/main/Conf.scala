package main

import java.nio.file.{FileSystems, Path}
import java.util.Properties
import javax.naming.directory.{InitialDirContext, SearchControls}

import pureconfig._

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

final case class AD(setup: Map[String, String])
final case class Search(base: String,
                        attribs: List[String],
                        filterMemberOfTemplate: String,
                        filterUserTemplate: String,
                        group: String)
final case class Conf(search: Search, ad: AD, outputFileLoc: Path)

object Conf {
  private implicit val pathConvert =
    ConfigConvert.fromString[Path](s => Try(FileSystems.getDefault.getPath(s)))
  private implicit def hint[T] =
    ConfigFieldMapping[T](ConfigFieldMapping(CamelCase, KebabCase))

  private def getContext(implicit ps: Properties): InitialDirContext = {
    Try { new InitialDirContext(ps) } match {
      case Success(c) => c
      case Failure(e) => throw e
    }
  }

  implicit lazy val conf: Conf =
    pureconfig.loadConfig[Conf].fold(e => throw e, c => c)

  implicit lazy val base: String = conf.search.base

  implicit lazy val props = new Properties()
  props.putAll(conf.ad.setup.asJava)

  implicit lazy val ctrl = new SearchControls()
  ctrl.setReturningAttributes(
    Option(conf.search.attribs.isEmpty)
      .filter(identity)
      .fold(conf.search.attribs.toArray)(_ => null)
  )
  ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE)

  implicit lazy val ctx: InitialDirContext = getContext
}
