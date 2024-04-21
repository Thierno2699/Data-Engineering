import models.DroneData

import scala.util.Random

object Simulator {
  private val random = new Random()

  // Rapport IoT avec des données aléatoires créé en fonction du paramètre
  def generateReport(Id: Int): DroneData = {
    val id = Id
    val latitude = -90 + random.nextDouble() * (90 - (-90))
    val longitude = -180 + random.nextDouble() * (180 - (-180))
    val frequence_cardiaque = random.nextInt(150)
    val temperature_corporelle = random.nextDouble() * 10 + 35
    val temperature = -20 + random.nextDouble() * (50 - (-20))
    val heure = "12:00"

    DroneData(id,latitude, longitude, frequence_cardiaque, temperature_corporelle, temperature,heure)
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
    println("- - - - - - - - - - - - - - - - - - - - - - - - - ")
  }

  // Simulation du générateur de rapports
  def simulate(startingId: Int, reportCount: Int): Unit = {
    val finalId = startingId + reportCount
    (startingId until finalId).foreach { id =>
      val report = generateReport(id)
      printReport(report)
      Thread.sleep(15000)
    }
  }

  def main(args: Array[String]): Unit = {
    val startingId = 1
    val reportCount = 3

    simulate(startingId, reportCount)
  }

}
