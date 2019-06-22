package services.proveedor

import models.proveedor.Proveedor
import models.categoria.Categoria
import repositories.proveedor.ProveedorRepositoryComponent
import play.api.libs.ws._
import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder
import scala.concurrent.Future
import play.api.libs.json._
import play.api.Play.current
import play.api.Logger

trait ProveedorServiceComponent {
    
    val proveedorService: ProveedorService
    
    trait ProveedorService {
        
        def createProveedor(proveedor: Proveedor): Proveedor
        
        //def updateUser(user: User)
        
        def tryFindByUser(user: String): Proveedor

        def list(): List[Proveedor]

        def listByCategoria(categoria:String): List[Proveedor]
        
        //def delete(id: Long)
    
    }

}

trait ProveedorServiceComponentImpl extends ProveedorServiceComponent {
    self: ProveedorRepositoryComponent =>
    
    override val proveedorService = new ProveedorServiceImpl
    
    class ProveedorServiceImpl extends ProveedorService {
        
        override def createProveedor(proveedor: Proveedor): Proveedor = {
            val data = Json.obj(
              "username" -> proveedor.usuario.user,
              "password" -> proveedor.usuario.password
            )
            val futureResponse: Future[WSResponse] = WS.url("http://ajustadoati.com:9090/plugins/restapi/v1/users").withHeaders("Authorization" -> ("EX5JIBA5lUfz3GSD")).post(data)
            Logger.info("response: "+futureResponse.toString)
           // Logger.info("response: "+futureResponse)
            proveedorRepository.createProveedor(proveedor)
        }
        /*
        override def updateUser(user: User) {
            userRepository.updateUser(user)
        }

        override def delete(id: Long) {
            userRepository.delete(id)
        }*/
        
        override def tryFindByUser(user: String): Proveedor = {
            proveedorRepository.tryFindByUser(user)
        }

        override def list(): List[Proveedor] = {
            proveedorRepository.list()
        }
        
         override def listByCategoria(categoria: String): List[Proveedor]={
           proveedorRepository.listByCategoria(categoria:String)
        }
        
    }
}