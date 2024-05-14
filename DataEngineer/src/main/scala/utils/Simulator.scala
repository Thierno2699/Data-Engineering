import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import models.DroneData
import parsers.Parsers

import scala.util.Random

object Simulator {
  private val random = new Random()
  private val kafkaTopic = "drone_data"

  // Configurer les propriétés Kafka
  private val props = new Properties()
  props.put("bootstrap.servers", "localhost:29092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  // Créer un producteur Kafka
  private val producer = new KafkaProducer[String, String](props)

  // Rapport IoT avec des données aléatoires créé en fonction du paramètre
  def generateReport(Id: Int): DroneData = {
    val id = Id
    val latitude = -90 + random.nextDouble() * (90 - (-90))
    val longitude = -180 + random.nextDouble() * (180 - (-180))
    val frequence_cardiaque = random.nextInt(150)
    val temperature_corporelle = random.nextDouble() * 10 + 35
    val temperature = -20 + random.nextDouble() * (50 - (-20))
    val heure = "12:00"
    val alerte = if (frequence_cardiaque > 100) true else false
    DroneData(id, latitude, longitude, frequence_cardiaque, temperature_corporelle, temperature, heure, alerte)
  }

  // Affiche un rapport IoT
  def printReport(report: DroneData): Unit = {
    println(s"Id: ${report.id}")
    println(s"Latitude: ${report.latitude}")
    println(s"Longitude: ${report.longitude}")
    println(s"Frequence cardiaque: ${report.frequence_cardiaque}")
    println(s"Temperature corporelle: ${report.temperature_corporelle}")
    println(s"Temperature: ${report.temperature}")
    println(s"Heure: ${report.heure}")
    println(s"Alerte: ${report.alerte}")
    println("- - - - - - - - - - - - - - - - - - - - - - - - - ")
  }

  // Envoyer un rapport au topic Kafka


  def sendReportToKafka(report: DroneData): Unit = {
    val jsonReport = Parsers.toJson(report) // Convertir le rapport en JSON
    val record = new ProducerRecord[String, String](kafkaTopic, report.id.toString, jsonReport)
    producer.send(record)
  }

  // Simulation du générateur de rapports
  def simulate(startingId: Int, reportCount: Int): Unit = {
    val finalId = startingId + reportCount
    (startingId until finalId).foreach { id =>
      val report = generateReport(id)
      sendReportToKafka(report) // Envoyer le rapport à Kafka
      printReport(report)
      Thread.sleep(3000)
    }
  }

  def main(args: Array[String]): Unit = {
    val startingId = 1
    val reportCount = 10

    simulate(startingId, reportCount)
  }

}
