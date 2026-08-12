package pruebaTechShop.Armando.service;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pruebaTechShop.Armando.domain.EstadoFactura;
import pruebaTechShop.Armando.domain.Factura;
import pruebaTechShop.Armando.domain.Item;
import pruebaTechShop.Armando.domain.Usuario;
import pruebaTechShop.Armando.domain.Venta;
import pruebaTechShop.Armando.repository.FacturaRepository;
import pruebaTechShop.Armando.repository.VentaRepository;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final VentaRepository ventaRepository;
    private final CarritoService carritoService;

    public FacturaService(FacturaRepository facturaRepository, VentaRepository ventaRepository, CarritoService carritoService) {
        this.facturaRepository = facturaRepository;
        this.ventaRepository = ventaRepository;
        this.carritoService = carritoService;
    }

    @Transactional(readOnly = true)
    public Factura getFacturaConVentas(Integer idFactura) {
        return facturaRepository.findByIdFacturaConDetalle(idFactura)
                .orElseThrow(() -> new NoSuchElementException("Factura con ID " + idFactura + " no encontrada."));
    }

    // Convierte el carrito de la sesion en una Factura + sus Ventas, y vacia el carrito.
    @Transactional
    public Factura facturar(HttpSession session, Usuario usuario) {
        List<Item> carrito = carritoService.obtenerCarrito(session);
        if (carrito.isEmpty()) {
            throw new IllegalStateException("El carrito esta vacio.");
        }

        Factura factura = new Factura();
        factura.setUsuario(usuario);
        factura.setFecha(LocalDateTime.now());
        factura.setEstado(EstadoFactura.Pagada);
        factura.setTotal(carritoService.getTotal(session));
        factura = facturaRepository.save(factura);

        for (Item item : carrito) {
            Venta venta = new Venta();
            venta.setFactura(factura);
            venta.setProducto(item.getProducto());
            venta.setPrecioHistorico(item.getPrecioHistorico());
            venta.setCantidad(item.getCantidad());
            ventaRepository.save(venta);
        }

        carritoService.vaciar(session);
        return factura;
    }
}
