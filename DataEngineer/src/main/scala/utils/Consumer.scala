package utils

import java.util.Properties
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import scala.collection.JavaConverters._
import scala.util.{Try, Success, Failure}
object Consumer {
  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest") // Définir le décalage sur le début du topic


    val consumer = new KafkaConsumer[String, String](props)
    consumer.subscribe(List("drone_data").asJava)

    processRecords(consumer)
  }

  def processRecords(consumer: KafkaConsumer[String, String]): Unit = {
    Try(consumer.poll(java.time.Duration.ofMillis(100))) match {
      case Success(records) =>
        records.asScala.foreach { record =>
          println(s"Clé: ${record.key()}, Valeur: ${record.value()}, Offset: ${record.offset()}, Partition: ${record.partition()}")
        }
        processRecords(consumer) // Récursivité pour continuer le traitement

      case Failure(exception) =>
        println(s"Erreur lors de la réception des données: ${exception.getMessage}")
        consumer.close() // Fermer le consommateur proprement en cas d'échec
    }
  }
}