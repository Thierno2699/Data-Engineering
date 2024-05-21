import models.DroneData
import parsers.Parsers

object Main {
  def maihn(args: Array[String]): Unit = {

    // rapport
    val report = DroneData(1, 48.8566, 2.3522, 60, 37.0, 20.0, "12:00", false)

    // Sérialiseur JSON
    val json = Parsers.toJson(report)
    println("sérialisé en JSON : " + json)

    // Désérialiseur JSON
    val deserializedReport = Parsers.fromJson(json)
    println("désérialisé du JSON: " + deserializedReport)

    // Sérialiseur CSV
    val csv = Parsers.toCsv(deserializedReport)
    println("sérialisé en CSV : " + csv)

    // Désérialiseur CSV
    val deserializedReportFromCsv = Parsers.fromCsv(csv)
    println("désérialisé à partir du CSV : " + deserializedReportFromCsv)
  }
}
