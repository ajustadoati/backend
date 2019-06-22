package services.dispositivo

import models.dispositivo.Dispositivo
import models.dispositivoUsuario.DispositivoUsuario
import repositories.dispositivo.DispositivoRepositoryComponent

trait DispositivoServiceComponent {
    
    val dispositivoService: DispositivoService
    
    trait DispositivoService {
        
        def createDispositivoUsuario(dispositivoUsuario: DispositivoUsuario): DispositivoUsuario

        def listDispositivoByUsuario(usuario:String): List[Dispositivo]

        def getDispositivoByUuid(uuid:String):Dispositivo

        def listDispositivoByListUsuarios(listUsuarios:String): List[DispositivoUsuario]

    }

}

trait DispositivoServiceComponentImpl extends DispositivoServiceComponent {
    self: DispositivoRepositoryComponent =>
    
    override val dispositivoService = new DispositivoServiceImpl
    
    class DispositivoServiceImpl extends DispositivoService {
        
        override def createDispositivoUsuario(dispositivoUsuario: DispositivoUsuario): DispositivoUsuario = {
            dispositivoRepository.createDispositivoUsuario(dispositivoUsuario)
        }
        

        override def listDispositivoByUsuario(usuario:String): List[Dispositivo] = {
            dispositivoRepository.listDispositivoByUsuario(usuario)
        }

        override def getDispositivoByUuid(uuid:String): Dispositivo = {
            dispositivoRepository.getDispositivoByUuid(uuid)
        }

        override def listDispositivoByListUsuarios(listUsuarios:String): List[DispositivoUsuario] = {
            dispositivoRepository.listDispositivoByListUsuarios(listUsuarios)
        }
        
        
        
    }
}