package utils

import java.util.Properties
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import parsers.Parsers.fromJson

import scala.collection.JavaConverters._
import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model._
import software.amazon.awssdk.regions.Region


object AlertSystem {
  private val kafkaTopic = "drone_data"
  private val bootstrapServers = "localhost:29092"
  private val awsRegion = "eu-west-3"
  private val sesClient = SesClient.builder.region(Region.of(awsRegion)).build()

  // Configurer les propriétés Kafka
  private val kafkaProps = new Properties()
  kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, "alert-system")

  // Créer un consommateur Kafka
  private val consumer = new KafkaConsumer[String, String](kafkaProps)

  // Abonner le consommateur au topic Kafka
  consumer.subscribe(java.util.Collections.singletonList(kafkaTopic))

  // Fonction pour envoyer un e-mail via Amazon SES
  private def sendEmail(subject: String, body: String): Unit = {
    val request = SendEmailRequest.builder()
      .destination(Destination.builder().toAddresses("corentin.henrion6@gmail.com").build())
      .message(Message.builder()
        .subject(Content.builder().data(subject).build())
        .body(Body.builder().text(Content.builder().data(body).build()).build())
        .build())
      .source("corentin.henrion6@gmail.com")
      .build()

    sesClient.sendEmail(request)
  }

  def main(args: Array[String]): Unit = {
    while (true) {
      val records = consumer.poll(java.time.Duration.ofMillis(100))
      records.asScala.foreach { record =>
        val jsonString = record.value()
        val droneData = fromJson(jsonString) // Assurez-vous de définir la logique de désérialisation appropriée ici

        if (droneData.alerte) {
          val subject = "Alerte de drone détectée"
          val body = s"Fréquence cardiaque trop élevé (${droneData.frequence_cardiaque} BPM) détecté pour le drone ${droneData.id}. Latitude: ${droneData.latitude}, Longitude: ${droneData.longitude}."
          sendEmail(subject, body)
        }
      }
    }
  }
}
