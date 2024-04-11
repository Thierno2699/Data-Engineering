package parsers

import models.DroneData
import play.api.libs.json._



object Parsers {
  // sérialisation en JSON
  def toJson(report: DroneData): String = {
    Json.toJson(report).toString()

  }
  // désérialisation en JSON
  def fromJson(json: String): DroneData = {
    Json.parse(json).as[DroneData]
  }

  // sérialisation en CSV
  def toCsv(report: DroneData): String = {
    s"${report.id},${report.latitude},${report.longitude},${report.frequence_cardiaque},${report.temperature_corporelle},${report.temperature},${report.heure}"
  }
  //désérialisation en CSV
  def fromCsv(csv: String): DroneData = {
    val fields = csv.split(",")
    DroneData(fields(0).toInt, fields(1).toDouble, fields(2).toDouble, fields(3).toDouble, fields(4).toDouble, fields(5).toDouble, fields(6).toString)
  }
}
