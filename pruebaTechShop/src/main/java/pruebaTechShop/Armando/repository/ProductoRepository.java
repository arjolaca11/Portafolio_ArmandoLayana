package pruebaTechShop.Armando.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pruebaTechShop.Armando.domain.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    public List<Producto> findByActivoTrue();
}
