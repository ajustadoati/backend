package services.usuario

import models.usuario.Usuario
import repositories.usuario.UsuarioRepositoryComponent
import play.api.libs.json._
import play.api.Play.current
import play.api.Logger
import scala.concurrent.Future
import scala.concurrent._
import ExecutionContext.Implicits.global
import play.api.libs.ws._
import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder
import scala.concurrent.duration._

trait UsuarioServiceComponent {
    
    val usuarioService: UsuarioService
    
    trait UsuarioService {
        
        def createUsuario(usuario: Usuario): Usuario

        def list(): List[Usuario]

        def tryFindByUser(user:String):Usuario

        def tryFindByUserAndPassword(user:String, password:String):Usuario

        def addContactToUser(contact:String, user:String):String

        def setPasswordForUser(user:String, password:String):Boolean

        def tryFindByUserAndEmail(user:String, password:String):Usuario

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
        
        override def setPasswordForUser(user: String, password:String): Boolean = {
            Logger.info("Set Password...")
            deleteUser(user)
            val result = addUser(user,password)
            usuarioRepository.setPasswordByUser(user,password)
            if(result == "Ok")
                return true
            else
                return false
        }

        override def tryFindByUserAndEmail(user:String, email:String): Usuario = {
            usuarioRepository.tryFindByUserAndEmail(user,email)
        }

        def deleteUser(user:String): String = {
             Logger.info("Deleting user ...")
            val future:Future[String] = WS.url("http://ajustadoati.com:9090/plugins/restapi/v1/users/"+user).withHeaders("Authorization" -> ("EX5JIBA5lUfz3GSD")).delete().map(_.statusText)
             val x = Await.result(future, 3.seconds)

            if(x=="Created")
                return "Created"
            else
                return "Error"
            
        }

        def addUser(user:String, password:String):String = {
            Logger.info("Adding user ...")
            val data = Json.obj(
              "username" -> user,
              "password" -> password
            )
            
            val future:Future[String] = WS.url("http://ajustadoati.com:9090/plugins/restapi/v1/users").withHeaders("Authorization" -> ("EX5JIBA5lUfz3GSD")).post(data).map(_.statusText)

            
            val x = Await.result(future, 3.seconds)

            if(x=="Created")
                return "Ok"
            else
                return "Ko"
           
        }


    }
}