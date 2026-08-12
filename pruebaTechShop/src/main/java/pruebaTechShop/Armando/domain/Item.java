package pruebaTechShop.Armando.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// No es una entidad JPA: representa una linea del carrito, guardada en la sesion HTTP.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private Producto producto;
    private int cantidad;
    private BigDecimal precioHistorico;

    public BigDecimal getSubTotal() {
        return producto.getPrecio().multiply(new BigDecimal(cantidad));
    }
}
