package models.criptomonedaUsuario

import play.api.libs.json.Json
import models.criptomoneda.Criptomoneda


 case class CriptomonedaUsuario(criptomoneda:Criptomoneda, usuario:String)

