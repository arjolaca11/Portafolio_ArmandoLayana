package pruebaTechShop.Armando.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pruebaTechShop.Armando.domain.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    // Trae la factura junto con su usuario, sus ventas y el producto de cada venta en una sola consulta
    @Query("SELECT f FROM Factura f "
            + "LEFT JOIN FETCH f.usuario "
            + "LEFT JOIN FETCH f.ventas v "
            + "LEFT JOIN FETCH v.producto "
            + "WHERE f.idFactura = :idFactura")
    public Optional<Factura> findByIdFacturaConDetalle(@Param("idFactura") Integer idFactura);
}
