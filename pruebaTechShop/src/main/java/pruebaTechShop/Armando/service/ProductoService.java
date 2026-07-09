package pruebaTechShop.Armando.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pruebaTechShop.Armando.domain.Producto;
import pruebaTechShop.Armando.repository.ProductoRepository;

@Service
public class ProductoService {

    // el repositorio es final para asegurar la inmutabilidad
    private final ProductoRepository productoRepository;
    private final FirebaseStorageService firebaseStorageService;

    // inyeccion por constructor (no requiere @Autowired en Spring moderno)
    public ProductoService(ProductoRepository productoRepository, FirebaseStorageService firebaseStorageService) {
        this.productoRepository = productoRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {
        if (activo) { //solo activos...
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Transactional
    public void save(Producto producto, MultipartFile imagenFile) {
        producto = productoRepository.save(producto);
        if (!imagenFile.isEmpty()) { //Si no esta vacio... pasaron una imagen...
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "producto", producto.getIdProducto());
                producto.setRutaImagen(rutaImagen);
                productoRepository.save(producto);
            } catch (IOException e) {

            }
        }
    }

    @Transactional
    public void delete(Integer idProducto) {
        // Verifica si el producto existe antes de intentar eliminarlo
        if (!productoRepository.existsById(idProducto)) {
            // Lanza una excepcion para indicar que el producto no fue encontrado
            throw new IllegalArgumentException("El producto con ID " + idProducto + " no existe.");
        }
        try {
            productoRepository.deleteById(idProducto);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepcion para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar el producto. Tiene datos asociados.", e);
        }
    }
}
