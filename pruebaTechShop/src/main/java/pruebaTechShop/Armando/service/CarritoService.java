package pruebaTechShop.Armando.service;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pruebaTechShop.Armando.domain.Item;
import pruebaTechShop.Armando.domain.Producto;

// El carrito de compras vive en la sesion HTTP (no en la base de datos) hasta que se factura.
@Service
public class CarritoService {

    public static final String ATRIBUTO_CARRITO = "carrito";

    @SuppressWarnings("unchecked")
    public List<Item> obtenerCarrito(HttpSession session) {
        List<Item> carrito = (List<Item>) session.getAttribute(ATRIBUTO_CARRITO);
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(ATRIBUTO_CARRITO, carrito);
        }
        return carrito;
    }

    // Agrega un producto al carrito asumiendo cantidad = 1; si ya estaba, incrementa la cantidad.
    public void agregar(HttpSession session, Producto producto) {
        List<Item> carrito = obtenerCarrito(session);
        Optional<Item> existente = carrito.stream()
                .filter(i -> i.getProducto().getIdProducto().equals(producto.getIdProducto()))
                .findFirst();
        if (existente.isPresent()) {
            existente.get().setCantidad(existente.get().getCantidad() + 1);
        } else {
            carrito.add(new Item(producto, 1, producto.getPrecio()));
        }
    }

    public void eliminar(HttpSession session, Integer idProducto) {
        obtenerCarrito(session).removeIf(i -> i.getProducto().getIdProducto().equals(idProducto));
    }

    public void actualizar(HttpSession session, Integer idProducto, int cantidad) {
        obtenerCarrito(session).stream()
                .filter(i -> i.getProducto().getIdProducto().equals(idProducto))
                .findFirst()
                .ifPresent(i -> i.setCantidad(cantidad));
    }

    public BigDecimal getTotal(HttpSession session) {
        return obtenerCarrito(session).stream()
                .map(Item::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void vaciar(HttpSession session) {
        session.removeAttribute(ATRIBUTO_CARRITO);
    }
}
