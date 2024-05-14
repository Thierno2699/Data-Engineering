ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.10"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.8.0"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.32"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-sns" % "1.12.78"
libraryDependencies += "software.amazon.awssdk" % "ses" % "2.17.34"
libraryDependencies += "software.amazon.awssdk" % "kinesis" % "2.17.34"


lazy val root = (project in file("."))
  .settings(
    name := "projet"
  )
