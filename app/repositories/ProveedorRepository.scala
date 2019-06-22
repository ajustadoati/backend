package repositories.proveedor

import models.proveedor.Proveedor
import models.categoria.Categoria
import models.usuario.Usuario
import play.api.libs.json.Json
import org.anormcypher._
import play.api.Logger
import models.connection.Connection

trait ProveedorRepositoryComponent {
    val proveedorRepository: ProveedorRepository
    
    trait ProveedorRepository {
        
        def createProveedor(proveedor: Proveedor): Proveedor
        
        //def updateProveedor(proveedor: Proveedor)
        
        def tryFindByUser(user: String): Proveedor
        
        //def delete(id: Long)

        def list(): List[Proveedor]

        def listByCategoria(categoria:String): List[Proveedor]
        
    }
}

trait ProveedorRepositoryComponentImpl extends ProveedorRepositoryComponent with Connection{
    override val proveedorRepository = new ProveedorRepositoryImpl
     
    
    
    class ProveedorRepositoryImpl extends ProveedorRepository {
        
        
        
       override def createProveedor(proveedor: Proveedor): Proveedor = {
            Logger.info("Guardando proveedor"+proveedor)
            val cat=Categoria("nueva", "nueva")
            val usuario=proveedor.usuario
            val result = Cypher("""CREATE (aa:Usuario {nombre:{nombre}, email:{email}, latitud:{latitud}, longitud:{longitud}, user:{user}, password:{password}, telefono:{telefono}})""").on("nombre"->usuario.nombre, "email"->usuario.email,"latitud"->usuario.latitud.toFloat, "longitud"->usuario.longitud.toFloat, "user"->usuario.user, "password"->usuario.password, "telefono"->usuario.telefono).execute()
            
            //val result = Cypher("""CREATE (aa:Proveedor {nombre:{nombre}, email:{email}, latitud:{latitud}, longitud:{longitud}, user:{user}, password:{password}, twitter:{twitter}})""").on("nombre"->proveedor.nombre, "email"->proveedor.email,"latitud"->proveedor.latitud.toFloat, "longitud"->proveedor.longitud.toFloat, "user"->proveedor.user, "password"->proveedor.password, "twitter"->proveedor.twitter).execute()
            for(categoria <- proveedor.categorias) { 
                Logger.info("insertando categoria"+categoria.nombre)
                val res=Cypher("""Match (pr:Usuario) Where pr.user={user} MATCH (ee:Categoria) WHERE ee.nombre = {nombre} Create (pr)-[:ATIENDE]->(ee)""").on("user"->usuario.user, "nombre"->categoria.nombre).execute() 
                Logger.info("insertando categoria result"+res)
            }
            if(result==true)
                return proveedor
            else
                return return Proveedor(Usuario("nombre", "email", 89222, 82272, "user", "password", "04127808193"), List(cat))
        }
        /*
        override def updateProveedor(proveedor: Proveedor) {
            //users.put(user.id.get, user)
        }

        override def delete(id: Long) {
            users.remove(id)
        }*/
        
        override def tryFindByUser(user: String): Proveedor = {
            //Option(proveedores.get(id))
            Logger.info("Buscando Proveedor"+user)         
            val cat=Categoria("nueva", "nueva") 
            val allProveedores= Cypher("MATCH (n:Usuario) WHERE n.user={user} RETURN n.nombre as nombre, n.email as email, n.latitud as latitud, n.longitud as longitud, n.user as user, n.password as password, n.twitter as twitter").on("user"->user)().map{     
                case CypherRow(nombre: String, email: String, latitud:BigDecimal,longitud:BigDecimal, user:String, password:String, telefono:String)=>Proveedor(Usuario(nombre, email, latitud, longitud, user, password, telefono), List(cat))     
            }

            val lista=allProveedores.toList
            if(lista.size>0)
                    return lista.iterator.next
                else
                    return Proveedor(Usuario("nombre", "email", 89222, 82272, "user", "password", "04127808193"), List(cat))
            
        
        }
        
        

        override def list(): List[Proveedor]={
            Logger.info("BUscando data")
             val cat=Categoria("nueva", "nueva") 
            val allProveedores = Cypher("MATCH (n:Usuario) RETURN n.nombre as nombre, n.email as email, n.latitud as latitud, n.longitud as longitud, n.user as user, n.password as password, n.telefono as telefono")().collect{

                case CypherRow(nombre: String, email: String, latitud:BigDecimal, longitud:BigDecimal, user:String, password:String, telefono:String)=>Proveedor(Usuario(nombre, email, latitud, longitud, user, password, telefono), List(cat))
                
            }
            val lista=allProveedores.toList
            Logger.info("lista"+lista)
            return lista
        }


        override def listByCategoria(categoria: String): List[Proveedor]={
            Logger.info("Buscando proveeedores"+categoria)
             val cat=Categoria("nueva", "nueva") 
             //MATCH (ca:Categoria {nombre: "Audio y Video"})<-[:ATIENDE]-(p:Proveedor) RETURN p.nombre
            val allProveedores = Cypher("MATCH (ca:Categoria {nombre: {categoria}})<-[:ATIENDE]-(n:Usuario) RETURN n.nombre as nombre, n.email as email, n.latitud as latitud, n.longitud as longitud, n.user as user, n.password as password, n.telefono as telefono").on("categoria"->categoria)().collect{

                case CypherRow(nombre: String, email: String, latitud:BigDecimal, longitud:BigDecimal, user:String, password:String, telefono:String)=>Proveedor(Usuario(nombre, email, latitud, longitud, user, password, telefono), List(cat))
                
            }
            val lista=allProveedores.toList
            Logger.info("lista"+lista)
            return lista
        }
        
    }
    
}