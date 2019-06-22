package repositories.categoria


import models.categoria.Categoria
import play.api.libs.json.Json
import org.anormcypher._
import play.api.Logger
import models.connection.Connection
trait CategoriaRepositoryComponent {
    val categoriaRepository: CategoriaRepository
    
    trait CategoriaRepository {
        
        def createCategoria(categoria: Categoria): Categoria

        def list(): List[Categoria]
        
    }
}

trait CategoriaRepositoryComponentImpl extends CategoriaRepositoryComponent with Connection{
    override val categoriaRepository = new CategoriaRepositoryImpl
     
   
    
    class CategoriaRepositoryImpl extends CategoriaRepository {
        
        
        
       override def createCategoria(categoria: Categoria): Categoria = {
            Logger.info("Guardando categoria"+categoria)
            val result = Cypher("""CREATE (aa:Categoria {nombre:{nombre}, descripcion:{descripcion}})""").on("nombre"->categoria.nombre, "descripcion"->categoria.descripcion).execute()
          
            if(result==true)
                return categoria
            else
                return return Categoria("nombre", "descripcion")
        }
        /*
        override def updateProveedor(proveedor: Proveedor) {
            //users.put(user.id.get, user)
        }

        override def delete(id: Long) {
            users.remove(id)
        }*/
        

        override def list(): List[Categoria]={
            Logger.info("BUscando data")
            val allCategorias = Cypher("MATCH (n:Categoria) RETURN n.nombre as nombre, n.descripcion as descripcion")().collect{

                case CypherRow(nombre: String, descripcion:String)=>Categoria(nombre, descripcion)
                
            }
            val lista=allCategorias.toList
            Logger.info("lista"+lista)
            return lista
        }
        
    }
    
}