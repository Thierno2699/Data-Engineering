package utils

import org.apache.spark.sql.SparkSession
import com.amazonaws.auth.{AWSCredentials, AWSCredentialsProvider, DefaultAWSCredentialsProviderChain}

object S3DataLoader {
  def main(args: Array[String]): Unit = {
    val masterUrl = "local"

    val spark = SparkSession
      .builder()
      .appName("S3ToComputer")
      .master(masterUrl)
      .getOrCreate()

    // Utiliser le DefaultAWSCredentialsProviderChain pour récupérer les identifiants du fichier .aws/credentials
    val credentialsProvider: AWSCredentialsProvider = new DefaultAWSCredentialsProviderChain()
    val awsCredentials: AWSCredentials = credentialsProvider.getCredentials

    // Configuration des identifiants AWS
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", awsCredentials.getAWSAccessKeyId)
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", awsCredentials.getAWSSecretKey)

    val s3BucketPath = "s3a://data-eng-freq/data/"

    val jsonData = spark.read.json(s3BucketPath)

    val timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))

    val outputPath = s"Data/Data_$timestamp"

    jsonData.write
      .format("json")
      .save(outputPath)

    println(s"Les données ont été enregistrées dans le répertoire $outputPath")

    spark.stop()
  }
}
