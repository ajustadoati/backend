package controllers.proveedor

import play.api.libs.json._
import play.api.libs.json.Writes._
import play.api.libs.json.Reads._
import play.api.libs.json.util._
import play.api.mvc._
import services.proveedor.ProveedorServiceComponent
import models.proveedor.Proveedor
import services.categoria.CategoriaServiceComponent
import models.categoria.Categoria
import models.cliente.Cliente
import models.producto.Producto
import models.consulta.Consulta
import models.usuario.Usuario
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError


trait ProveedorController extends Controller {
    self: ProveedorServiceComponent =>
  implicit val categoriaWrites = Json.writes[Categoria]
  implicit val categoriaReads = Json.reads[Categoria]
   implicit val clienteWrites = Json.writes[Cliente]
  implicit val clienteReads = Json.reads[Cliente]

  implicit val productoWrites = Json.writes[Producto]
  implicit val productoReads = Json.reads[Producto]
      implicit val usuarioWritesP = Json.writes[Usuario]
  implicit val usuarioReadsP = Json.reads[Usuario]
  

implicit val proveedorWrites: Writes[Proveedor] = (
  (__ \ "usuario").write[Usuario] and
  (__ \ "categorias").lazyWrite(Writes.traversableWrites[Categoria](categoriaWrites))
)(unlift(Proveedor.unapply))

implicit val consultaWrites: Writes[Consulta] = (
  (__ \ "usuario").write[Usuario] and
  (__ \ "producto").write[Producto] and
  (__ \ "categoria").write[Categoria]
)(unlift(Consulta.unapply))

 implicit val consultaReads = Json.reads[Consulta]

  implicit val proveedorReads = Json.reads[Proveedor]
 

    def saveProveedor = Action(BodyParsers.parse.json) { request =>
    val b = request.body.validate[Proveedor]

    b.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      proveedor => {
        //addBook(book)
        Logger.info("guardando provedor"+proveedor)
        val p=proveedorService.createProveedor(proveedor)
        Created(Json.obj("status" -> "Registro Creado"))
      }
    )
  }
                              
    
    def findProveedorByUser(user: String) = Action {
      Logger.info("Controller: buscando usuario"+user)
        val proveedor = proveedorService.tryFindByUser(user)
       
            Ok(Json.toJson(proveedor))
       
    }
    
    def listProveedores = Action {

        Ok(Json.toJson(proveedorService.list()))
    }

    def listByCategoria(categoria:String) = Action {

        Ok(Json.toJson(proveedorService.listByCategoria(categoria)))
    }
     
    

}

