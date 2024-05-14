
package models
import play.api.libs.json.{Format, Json}

case class DroneData(
                       id: Int,
                       latitude: Double,
                       longitude: Double,
                       frequence_cardiaque: Double,
                       temperature_corporelle: Double,
                       temperature: Double,
                       heure: String,
                       alerte: Boolean
    )

object DroneData {
  implicit val format: Format[DroneData] = Json.format[DroneData]
}
