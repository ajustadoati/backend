package repositories.criptomoneda
import models.usuario.Usuario
import models.criptomonedaUsuario.CriptomonedaUsuario
import models.criptomoneda.Criptomoneda
import play.api.libs.json.Json
import org.anormcypher._
import play.api.Logger
import models.connection.Connection

trait CriptomonedaRepositoryComponent {
    val criptomonedaRepository: CriptomonedaRepository
    
    trait CriptomonedaRepository {

        def createCriptomonedaUsuario(criptomonedaUsuario: CriptomonedaUsuario): CriptomonedaUsuario

        def listCriptomonedaByUsuario(usuario:String): List[Criptomoneda]
        
    }
}

trait CriptomonedaRepositoryComponentImpl extends CriptomonedaRepositoryComponent with Connection{
    override val criptomonedaRepository = new CriptomonedaRepositoryImpl

    class CriptomonedaRepositoryImpl extends CriptomonedaRepository {

       override def createCriptomonedaUsuario(criptomonedaUsuario: CriptomonedaUsuario): CriptomonedaUsuario = {
        
        val usuario: String=criptomonedaUsuario.usuario
        val criptomoneda: Criptomoneda=criptomonedaUsuario.criptomoneda

            val allUsuarios= Cypher("MATCH (n:Usuario) WHERE n.user={user} RETURN n.nombre as nombre, n.email as email, n.latitud as latitud, n.longitud as longitud, n.user as user, n.password as password, n.telefono as telefono").on("user"->usuario)().map{     
                case CypherRow(nombre: String, email: String, latitud:BigDecimal,longitud:BigDecimal, user:String, password:String, telefono:String)=>Usuario(nombre, email, latitud, longitud, user, password, telefono)     
            }
            val lista=allUsuarios.toList
            
            if(lista.size==0)
                return null
                
            val result = Cypher("""MATCH (usr:Usuario) WHERE usr.user={user} CREATE (dp:Criptomoneda {tipo:{tipo}, nombre:{nombre}, descripcion:{descripcion}, address:{address}}), (usr)-[:AGREGO]->(dp)""").on("user"->usuario, "tipo"->criptomoneda.tipo,"nombre"->criptomoneda.nombre, "descripcion"->criptomoneda.descripcion, "address" -> criptomoneda.address).execute()
            if(result==true)
                return criptomonedaUsuario
            else 
                return null
        }

        override def listCriptomonedaByUsuario(usuario:String): List[Criptomoneda]={
            Logger.debug("Repository: Buscando data:"+usuario)
            val allCriptomonedas = Cypher("MATCH (cl:Usuario {user:{usuario}})-[r:AGREGO]->(n:Criptomoneda) RETURN n.tipo, n.nombre, n.descripcion, n.address").on("usuario"->usuario)().map{ 
              case CypherRow(tipo:String, nombre: String, descripcion: String, address: String)=>Criptomoneda(tipo,nombre,descripcion,address)
            }
           
            val lista=allCriptomonedas.toList
            Logger.info("lista"+lista)

            if(lista.size>0)
                    return lista
                else
                    return null
        }


    }
    
}