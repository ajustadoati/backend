package models.usuarioLogin

import play.api.libs.json.Json
import org.anormcypher._
import play.api.Logger


case class UsuarioLogin(user:String, password:String)
  
