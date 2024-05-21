ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.10"



// Dépendances Spark
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.1.1"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.1.1"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.17.0"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.17.0"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.17.1"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.17.0"

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.8.0"


// Dépendances AWS SDK
libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % "1.12.78"
libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "3.2.0"

// Autres dépendances existantes
libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.8.0"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.32"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-sns" % "1.12.78"
libraryDependencies += "software.amazon.awssdk" % "ses" % "2.17.34"
libraryDependencies += "software.amazon.awssdk" % "kinesis" % "2.17.34"
libraryDependencies += "software.amazon.awssdk" % "s3" % "2.15.40"

lazy val root = (project in file("."))
  .settings(
    name := "DataIng"
  )
