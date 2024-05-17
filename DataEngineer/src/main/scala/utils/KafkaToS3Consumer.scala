package utils

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import java.util.{Collections, Properties}
import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{PutObjectRequest}

import java.nio.charset.StandardCharsets

object KafkaToS3Consumer extends App {
  val props = new Properties()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "drone-data-consumer-group")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  val consumer = new KafkaConsumer[String, String](props)
  consumer.subscribe(Collections.singletonList("drone_data"))

  val s3: S3Client = S3Client.builder()
    .region(Region.EU_WEST_3)
    .credentialsProvider(DefaultCredentialsProvider.create())
    .build()

  def consumeAndUpload(): Unit = {
    Try(consumer.poll(java.time.Duration.ofMillis(1000))) match {
      case Success(records) =>
        records.asScala.foreach { record =>
          val key = record.key()
          val value = record.value()
          val putObjectRequest = PutObjectRequest.builder()
            .bucket("data-eng-freq")
            .key( "data/" + record.key() + "_" + System.currentTimeMillis() + ".json")
            .build()

          // Utilisation de RequestBody.fromString pour créer le corps de la requête
          val requestBody = RequestBody.fromString(value, StandardCharsets.UTF_8)

          Try(s3.putObject(putObjectRequest, requestBody)) match {
            case Success(response) =>
              println(s"Uploaded data to S3 with ETag: ${response.eTag()}")
            case Failure(ex) =>
              println(s"Failed to upload data to S3: ${ex.getMessage}")
          }
        }
        consumeAndUpload() // Appel récursif

      case Failure(ex) =>
        println(s"Failed to poll data from Kafka: ${ex.getMessage}")
    }
  }

  consumeAndUpload() // Démarrez la récursivité
  sys.addShutdownHook {
    consumer.close()
    s3.close()
    println("Closed Kafka consumer and S3 client")
  }
}
