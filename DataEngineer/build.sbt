ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.10"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.4"


lazy val root = (project in file("."))
  .settings(
    name := "DataEngineer"
  )
