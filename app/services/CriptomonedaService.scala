package services.criptomoneda

import models.criptomoneda.Criptomoneda
import models.criptomonedaUsuario.CriptomonedaUsuario
import repositories.criptomoneda.CriptomonedaRepositoryComponent

trait CriptomonedaServiceComponent {
    
    val criptomonedaService: CriptomonedaService
    
    trait CriptomonedaService {
        
        def createCriptomonedaUsuario(criptomonedaUsuario: CriptomonedaUsuario): CriptomonedaUsuario

        def listCriptomonedaByUsuario(usuario:String): List[Criptomoneda]

    }

}

trait CriptomonedaServiceComponentImpl extends CriptomonedaServiceComponent {
    self: CriptomonedaRepositoryComponent =>
    
    override val criptomonedaService = new CriptomonedaServiceImpl
    
    class CriptomonedaServiceImpl extends CriptomonedaService {
        
        override def createCriptomonedaUsuario(criptomonedaUsuario: CriptomonedaUsuario): CriptomonedaUsuario = {
            criptomonedaRepository.createCriptomonedaUsuario(criptomonedaUsuario)
        }
        

        override def listCriptomonedaByUsuario(usuario:String): List[Criptomoneda] = {
            criptomonedaRepository.listCriptomonedaByUsuario(usuario)
        }
        
    }
}