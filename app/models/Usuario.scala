package models.usuario

import play.api.libs.json.Json
import org.anormcypher._
import play.api.Logger


case class Usuario(nombre: String, email: String, latitud:BigDecimal, longitud:BigDecimal, user:String, password:String, telefono:String)
  

