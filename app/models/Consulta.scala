package models.consulta

import play.api.libs.json.Json
import models.producto.Producto
import models.categoria.Categoria
import models.usuario.Usuario



 case class Consulta(usuario: Usuario, producto:Producto, categoria:Categoria)

