package controllers.usuario

import play.api.libs.json._
import play.api.mvc._
import services.usuario.UsuarioServiceComponent
import services.usuario.UsuarioServiceComponentImpl
import repositories.usuario.UsuarioRepositoryComponentImpl
import models.usuario.Usuario
import play.api.Logger

trait UsuarioController extends Controller with UsuarioServiceComponentImpl with UsuarioRepositoryComponentImpl{
    self: UsuarioServiceComponent =>

  implicit val usuarioWrites = Json.writes[Usuario]
  implicit val usuarioReads = Json.reads[Usuario]

   
  def saveUsuario = Action(BodyParsers.parse.json) { request =>
      val b = request.body.validate[Usuario]

      b.fold(
        errors => {
          BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
        },
        usuario => {
       
          Logger.info("guardando usuario"+usuario)
          val p=usuarioService.createUsuario(usuario)
          Created(Json.obj("status" -> "Registro Creado"))
        }
      )
    }

    def listUsuarios = Action {
        Logger.info("obteniendo usuarios")
        Ok(Json.toJson(usuarioService.list()))
    }

    def findUsuarioByUser(user: String) = Action {
      Logger.info("Controller: buscando usuario"+user)
        val usuario = usuarioService.tryFindByUser(user)
       
            Ok(Json.toJson(usuario))
       
    }

    



    
  

}

