package models.proveedor

import play.api.libs.json.Json
import org.anormcypher._
import play.api.Logger
import models.categoria.Categoria
import models.usuario.Usuario

case class Proveedor(usuario:Usuario, categorias:List[Categoria])
  

