package models.dispositivoUsuario

import play.api.libs.json.Json
import models.dispositivo.Dispositivo


 case class DispositivoUsuario(dispositivo:Dispositivo, usuario:String)

