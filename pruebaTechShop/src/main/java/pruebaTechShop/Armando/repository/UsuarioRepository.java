package pruebaTechShop.Armando.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pruebaTechShop.Armando.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Optional<Usuario> findByUsername(String username);
}
