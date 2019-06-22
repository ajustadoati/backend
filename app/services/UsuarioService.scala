package services.usuario

import models.usuario.Usuario
import repositories.usuario.UsuarioRepositoryComponent
import play.api.libs.json._
import play.api.Play.current
import play.api.Logger
import scala.concurrent.Future

import play.api.libs.ws._
import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder

trait UsuarioServiceComponent {
    
    val usuarioService: UsuarioService
    
    trait UsuarioService {
        
        def createUsuario(usuario: Usuario): Usuario

        def list(): List[Usuario]

        def tryFindByUser(user:String):Usuario

        def tryFindByUserAndPassword(user:String, password:String):Usuario

        def addContactToUser(contact:String, user:String):String

    }

}

trait UsuarioServiceComponentImpl extends UsuarioServiceComponent {
    self: UsuarioRepositoryComponent =>
    
    override val usuarioService = new UsuarioServiceImpl
    
    class UsuarioServiceImpl extends UsuarioService {
        
        override def createUsuario(usuario: Usuario): Usuario = {
            val data = Json.obj(
              "username" -> usuario.user,
              "password" -> usuario.password
            )
            val futureResponse: Future[WSResponse] = WS.url("http://ajustadoati.com:9090/plugins/restapi/v1/users").withHeaders("Authorization" -> ("EX5JIBA5lUfz3GSD")).post(data)
            Logger.info("Guardando usuario en Jabber: "+futureResponse.toString)
            usuarioRepository.createUsuario(usuario)
        }
        

        override def list(): List[Usuario] = {
            usuarioRepository.list()
        }
        
        override def tryFindByUser(user: String): Usuario = {
            usuarioRepository.tryFindByUser(user)
        }

        override def tryFindByUserAndPassword(user: String, password:String): Usuario = {
            usuarioRepository.tryFindByUserAndPassword(user,password)
        }

        override def addContactToUser(contact:String, user:String):String = {
            usuarioRepository.addContactToUser(contact,user)
        }
        
        
    }
}