package pruebaTechShop.Armando.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pruebaTechShop.Armando.domain.Constante;

@Repository
public interface ConstanteRepository extends JpaRepository<Constante, Integer> {

    public Optional<Constante> findByAtributo(String atributo);
}
