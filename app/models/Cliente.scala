package models.cliente

import play.api.libs.json.Json


 case class Cliente(nombre: String, email: String, twitter:String, telefono:String, latitud: BigDecimal, longitud:BigDecimal)

