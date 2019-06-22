package repositories.consulta


import models.consulta.Consulta
import models.cliente.Cliente
import models.producto.Producto
import models.usuario.Usuario
import models.categoria.Categoria
import play.api.libs.json.Json
import org.anormcypher._
import play.api.Logger
import models.connection.Connection
trait ConsultaRepositoryComponent {
    val consultaRepository: ConsultaRepository
    
    trait ConsultaRepository {
        
        def createConsulta(consulta: Consulta): Long

        def list(): List[Consulta]

        def listByUser(user:String):List[Consulta]

        def getConsultaById(id:Long):Consulta
        
    }
}

trait ConsultaRepositoryComponentImpl extends ConsultaRepositoryComponent with Connection{
    override val consultaRepository = new ConsultaRepositoryImpl
     
   
    
    class ConsultaRepositoryImpl extends ConsultaRepository {
        
        
        
       override def createConsulta(consulta: Consulta): Long = {
        
        
        val producto: Producto=consulta.producto
        val categoria: Categoria=consulta.categoria

             val fecha = System.currentTimeMillis()
             Logger.info("fecha:"+fecha)
             val allUsuarios= Cypher("MATCH (n:Usuario) WHERE n.user={user} RETURN n.nombre as nombre, n.email as email, n.latitud as latitud, n.longitud as longitud, n.user as user, n.password as password, n.telefono as telefono").on("user"->consulta.usuario.user)().map{     
                case CypherRow(nombre: String, email: String, latitud:BigDecimal,longitud:BigDecimal, user:String, password:String, telefono:String)=>Usuario(nombre, email, latitud, longitud, user, password, telefono)     
            }

            val lista=allUsuarios.toList
            if(lista.size>0){
                Logger.info("El usuario existe")
                val usuario: Usuario = lista.iterator.next

                val id = Cypher("MATCH (c:Categoria) WHERE c.nombre={catnombre} MATCH (usr:Usuario) WHERE usr.user={user} CREATE (pr:Producto {nombre:{pnombre}, descripcion:{descripcion}}), (pr)-[:PERTENECE]->(c), (usr)-[rb:BUSCO {fecha:{fecha}}]->(pr) return id(rb) as id").on("catnombre"->categoria.nombre, "user"->usuario.user, "pnombre"->producto.nombre, "descripcion"->producto.descripcion, "fecha"->fecha)().map{
                    row => (row[Long]("id"))
                }

                val result=id.toList
            
                if(result.size > 0)
                    return result.iterator.next
                else
                    return 0
            }
            else{
                Logger.info("Se crea el usuario anonimo")
                val usuario: Usuario = consulta.usuario
                val id = Cypher("MATCH (c:Categoria) WHERE c.nombre={catnombre} CREATE (usr:Usuario { nombre: {nombre}, email: {email}, telefono:{telefono}, latitud:{latitud}, longitud:{longitud}, user:{user},password:{password}}) CREATE (pr:Producto {nombre:{pnombre}, descripcion:{descripcion}}), (pr)-[:PERTENECE]->(c), (usr)-[rb:BUSCO {fecha:{fecha}}]->(pr) return id(rb) as id").on("catnombre"->categoria.nombre, "nombre"->usuario.nombre, "email"->usuario.email, "telefono"->usuario.telefono, "latitud"->usuario.latitud.toDouble, "longitud"->usuario.longitud.toDouble, "user"->usuario.user,"password"->usuario.password, "pnombre"->producto.nombre, "descripcion"->producto.descripcion, "fecha"->fecha)().map{
                    row => (row[Long]("id"))
                }
                val result=id.toList
                if(result.size > 0)
                    return result.iterator.next
                else
                    return 0   
            } 
        }

        override def list(): List[Consulta]={
            Logger.info("BUscando data")
           

            val allConsultas = Cypher("MATCH (cl:Usuario)-[r:BUSCO]->(n:Producto)-[p:PERTENECE]->(c:Categoria) RETURN cl.nombre as nombre, cl.email as cemail, cl.user as cuser, cl.telefono as ctelefono, cl.latitud as clatitud, cl.longitud as clongitud, id(n) as id, n.nombre as pnombre, n.descripcion as pdescripcion, c.nombre as catnombre, c.descripcion as catdesc")().map { row =>
              (Consulta(Usuario(row[String]("nombre"),row[String]("cemail"),row[BigDecimal]("clatitud"),row[BigDecimal]("clongitud"),row[String]("cuser"),row[String]("ctelefono"),row[String]("ctelefono")),(Producto(row[Long]("id"), row[String]("pnombre"),row[String]("pdescripcion"))),Categoria(row[String]("catnombre"),row[String]("catdesc"))))
            }
           
            val lista=allConsultas.toList
            Logger.info("lista"+lista)
            return lista
        }

        override def listByUser(user:String):List[Consulta]={
            Logger.info("buscando data para usuario:"+user)

            
            val categorias = Cypher("Match (cl:Usuario {user:{usuario}})-[r:ATIENDE]->(c:Categoria) RETURN c.nombre as nombre").on("usuario"->user)().map{
                case CypherRow(nombre:String)=>(nombre)
            }
            Logger.info("categorias:"+categorias.toList)
    
            val consultas = Cypher("WITH {lista} as cats Match (u:Usuario)-[rb:BUSCO]->(p:Producto)-[r:PERTENECE]->(c:Categoria) Where c.nombre in cats return u.nombre as nombre,u.user as user, u.email as email, u.telefono as telefono, u.latitud as latitud, u.longitud as longitud, id(rb) as id, p.nombre as pnombre, p.descripcion as pdescripcion, c.nombre as cnombre, c.descripcion as cdesc ORDER BY id(rb) DESC limit 2").on("lista"->categorias.toList)().map { row =>              
                (Consulta(Usuario(row[String]("nombre"),row[String]("email"),row[BigDecimal]("latitud"),row[BigDecimal]("longitud"),row[String]("user"),row[String]("telefono"),row[String]("telefono")),(Producto(row[Long]("id"), row[String]("pnombre"),row[String]("pdescripcion"))),Categoria(row[String]("cnombre"),row[String]("cdesc"))))
            }
            val lista=consultas.toList

            if(lista.size > 0){
                return lista
            }

            return null
            
        }

        override def getConsultaById(id:Long):Consulta={
            Logger.info("Repository: buscando data:"+id)
            val consultas = Cypher("Match (u:Usuario)-[rb:BUSCO]->(p:Producto)-[r:PERTENECE]->(c:Categoria) Where id(rb)={idConsulta} return u.nombre as nombre, u.email as email, u.user as user, u.telefono as telefono, u.latitud as latitud, u.longitud as longitud, id(rb) as id, p.nombre as pnombre, p.descripcion as pdescripcion, c.nombre as cnombre, c.descripcion as cdesc").on("idConsulta"->id)().map { row =>              
                (Consulta(Usuario(row[String]("nombre"),row[String]("email"),row[BigDecimal]("latitud"),row[BigDecimal]("longitud"),row[String]("user"),row[String]("telefono"),row[String]("telefono")),(Producto(row[Long]("id"), row[String]("pnombre"),row[String]("pdescripcion"))),Categoria(row[String]("cnombre"),row[String]("cdesc"))))
            }
            val lista=consultas.toList

            if(lista.size > 0){
                return lista.iterator.next
            }

            return null
        }
        
    }
    
}