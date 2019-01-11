name := "ADOperator"

version := "0.1"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.github.melrief" %% "pureconfig" % "0.5.1",
  "org.typelevel" %% "cats" % "0.9.0"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:existentials",
  "-Ywarn-dead-code",
  "-Ywarn-unused"
)
    