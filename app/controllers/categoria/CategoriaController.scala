package controllers.categoria

import play.api.libs.json._
import play.api.mvc._
import services.categoria.CategoriaServiceComponent
import services.categoria.CategoriaServiceComponentImpl
import repositories.categoria.CategoriaRepositoryComponentImpl
import models.categoria.Categoria
import play.api.Logger

trait CategoriaController extends Controller with CategoriaServiceComponentImpl with CategoriaRepositoryComponentImpl{
    self: CategoriaServiceComponent =>

  implicit val categoriaWrites = Json.writes[Categoria]
  implicit val categoriaReads = Json.reads[Categoria]

   
 

    def saveCategoria = Action(BodyParsers.parse.json) { request =>
    val b = request.body.validate[Categoria]

    b.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      categoria => {
     
        Logger.info("guardando categoria"+categoria)
        val p=categoriaService.createCategoria(categoria)
        Created(Json.obj("status" -> "Registro Creado"))
      }
    )
  }
    
    def listCategorias = Action {

        Ok(Json.toJson(categoriaService.list()))
    }
    
  

}

