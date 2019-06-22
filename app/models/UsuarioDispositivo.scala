package models.usuarioDispositivo

import play.api.libs.json.Json
import models.dispositivo.Dispositivo
import models.usuario.Usuario

 case class UsuarioDispositivo(dispositivo:Dispositivo, usuario:Usuario)

