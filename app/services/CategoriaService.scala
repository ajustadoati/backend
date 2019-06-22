package services.categoria

import models.proveedor.Proveedor
import models.categoria.Categoria
import repositories.categoria.CategoriaRepositoryComponent

trait CategoriaServiceComponent {
    
    val categoriaService: CategoriaService
    
    trait CategoriaService {
        
        def createCategoria(categoria: Categoria): Categoria

        def list(): List[Categoria]

    }

}

trait CategoriaServiceComponentImpl extends CategoriaServiceComponent {
    self: CategoriaRepositoryComponent =>
    
    override val categoriaService = new CategoriaServiceImpl
    
    class CategoriaServiceImpl extends CategoriaService {
        
        override def createCategoria(categoria: Categoria): Categoria = {
            categoriaRepository.createCategoria(categoria)
        }
        

        override def list(): List[Categoria] = {
            categoriaRepository.list()
        }
        
        
        
    }
}
