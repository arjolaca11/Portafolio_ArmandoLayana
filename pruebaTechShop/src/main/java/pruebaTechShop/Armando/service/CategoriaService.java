package pruebaTechShop.Armando.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pruebaTechShop.Armando.domain.Categoria;
import pruebaTechShop.Armando.repository.CategoriaRepository;

@Service
public class CategoriaService {

    // el repositorio es final para asegurar la inmutabilidad
    private final CategoriaRepository categoriaRepository;

    // inyeccion por constructor (no requiere @Autowired en Spring moderno)
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) { //solo activas...
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria).orElse(new Categoria());
    }

    @Transactional
    public void guardar(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    @Transactional
    public void eliminar(Integer idCategoria) {
        categoriaRepository.deleteById(idCategoria);
    }
}
