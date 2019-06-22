package repositories.usuario


import models.usuario.Usuario
import models.cliente.Cliente
import models.producto.Producto
import models.categoria.Categoria
import play.api.libs.json.Json
import org.anormcypher._
import play.api.Logger
import models.connection.Connection

/**
** Interface User
**/
trait UsuarioRepositoryComponent {
    val usuarioRepository: UsuarioRepository
    
    trait UsuarioRepository {
        
        def createUsuario(usuario: Usuario): Usuario

        def list(): List[Usuario]

        def tryFindByUser(user:String):Usuario

        def tryFindByUserAndPassword(user:String, password:String):Usuario

        def addContactToUser(contact:String, user:String):String
        
    }
}
/**Class User **/
trait UsuarioRepositoryComponentImpl extends UsuarioRepositoryComponent with Connection{
    override val usuarioRepository = new UsuarioRepositoryImpl
     
   
    
    class UsuarioRepositoryImpl extends UsuarioRepository {
        
        
        /*
        ** Method to create user
        **/
       override def createUsuario(usuario: Usuario): Usuario = {
        
            Logger.info("Repository: Saving user"+usuario)
            val fecha = System.currentTimeMillis()
            
            val result = Cypher("""CREATE (aa:Usuario {nombre:{nombre}, email:{email}, latitud:{latitud}, longitud:{longitud}, user:{user}, password:{password}, telefono:{telefono}, fecha:{fecha}})""").on("nombre"->usuario.nombre, "email"->usuario.email,"latitud"->usuario.latitud.toFloat, "longitud"->usuario.longitud.toFloat, "user"->usuario.user, "password"->usuario.password, "telefono"->usuario.telefono, "fecha"->fecha).execute()
             
            if(result==true)
                return usuario
            else
                return null
        
        }

        override def tryFindByUser(user: String): Usuario = {
            
            Logger.info("Buscando Usuario")         
            
            val allUsuarios= Cypher("MATCH (n:Usuario) WHERE n.user={user} RETURN n.nombre as nombre, n.email as email, n.latitud as latitud, n.longitud as longitud, n.user as user, n.password as password, n.telefono as telefono").on("user"->user)().map{     
                case CypherRow(nombre: String, email: String, latitud:BigDecimal,longitud:BigDecimal, user:String, password:String, telefono:String)=>Usuario(nombre, email, latitud, longitud, user, password, telefono)     
            }

            val lista=allUsuarios.toList
            if(lista.size>0)
                    return lista.iterator.next
                else
                    return null
        }

         override def tryFindByUserAndPassword(user: String, password:String): Usuario = {
            //Option(proveedores.get(id))
            Logger.info("Buscando Usuario por user y password")         
            
            val allUsuarios= Cypher("MATCH (n:Usuario) WHERE n.user={user} and n.password={password} RETURN n.nombre as nombre, n.email as email, n.latitud as latitud, n.longitud as longitud, n.user as user, n.password as password, n.telefono as telefono").on("user"->user, "password"->password)().map{     
                case CypherRow(nombre: String, email: String, latitud:BigDecimal,longitud:BigDecimal, user:String, password:String, telefono:String)=>Usuario(nombre, email, latitud, longitud, user, password, telefono)     
            }

            val lista=allUsuarios.toList
            if(lista.size>0)
                    return lista.iterator.next
                else
                    return null
        }

        override def addContactToUser(contact: String, user:String): String = {
            Logger.info("Agregando contacto")         
            
            val result = Cypher("MATCH (u:Usuario) WHERE u.user={user} MATCH (c:Usuario) WHERE c.user={contact} CREATE (u)-[:CONOCE]->(c)").on("user"->user, "contact"->contact).execute()

            
            if(result == true)
                return "OK"
            else
                return null
        }

        override def list(): List[Usuario]={
            Logger.info("BUscando data")
            val allUsuarios = Cypher("MATCH (n:Usuario) RETURN n.nombre as nombre, n.email as email, n.latitud as latitud, n.longitud as longitud, n.user as user, n.password as password, n.telefono as telefono")().collect{

                case CypherRow(nombre: String, email: String, latitud:BigDecimal, longitud:BigDecimal, user:String, password:String, telefono:String)=>Usuario(nombre, email, latitud, longitud, user, password, telefono)
                
            }
            val lista=allUsuarios.toList
            if(lista.size>0)
                    return lista
                else
                    return null
        }
        
    }
    
}