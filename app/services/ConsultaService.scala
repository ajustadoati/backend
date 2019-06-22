package services.consulta

import models.consulta.Consulta
import repositories.consulta.ConsultaRepositoryComponent

trait ConsultaServiceComponent {
    
    val consultaService: ConsultaService
    
    trait ConsultaService {
        
        def createConsulta(consulta: Consulta): Long

        def list(): List[Consulta]

        def listByUser(user:String):List[Consulta]

        def getConsultaById(id:Long):Consulta

    }

}

trait ConsultaServiceComponentImpl extends ConsultaServiceComponent {
    self: ConsultaRepositoryComponent =>
    
    override val consultaService = new ConsultaServiceImpl
    
    class ConsultaServiceImpl extends ConsultaService {
        
        override def createConsulta(consulta: Consulta): Long = {
            consultaRepository.createConsulta(consulta)
        }
        
        override def list(): List[Consulta] = {
            consultaRepository.list()
        }
        
        override def listByUser(user:String):List[Consulta]={
            consultaRepository.listByUser(user)
        }

        override def getConsultaById(id:Long):Consulta = {
                consultaRepository.getConsultaById(id)
        }
        
    }


}