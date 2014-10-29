import sbt._
import sbt.Keys._

object ApplicationBuild extends Build {

  val appName         = "app"
  val appVersion      = "0.1.0"
  val appScalaVersion = "2.10.4"

  val appResolvers = Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
  )

  val appDependencies = Seq(
    "com.twitter" %% "finagle-core" % "6.22.0",
    "com.twitter" %% "finagle-http" % "6.22.0",
    "org.specs2" %% "specs2" % "2.4.9" % "test"
  )

  lazy val main = Project(
    id = appName.toLowerCase,
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := appName,
      version := appVersion,
      scalaVersion := appScalaVersion,
      resolvers ++= appResolvers,
      libraryDependencies ++= appDependencies,
      scalacOptions ++= Seq("-encoding", "UTF-8", "-feature", "-deprecation", "-unchecked"),
      javacOptions ++= Seq("-encoding", "UTF-8")
    )
  )
}
