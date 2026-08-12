package pruebaTechShop.Armando;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pruebaTechShop.Armando.service.CarritoService;
import pruebaTechShop.Armando.service.ConstanteService;

// Reto sem05: la tabla "constante" guarda parametros del sistema (p.ej. el tamano
// maximo de imagen); este @ControllerAdvice lo expone a TODAS las paginas como
// atributo de modelo, para que rutinas.js valide contra un valor configurable en
// BD en lugar de un numero fijo en el codigo.
@ControllerAdvice
public class GlobalModelAdvice {

    private final ConstanteService constanteService;
    private final CarritoService carritoService;

    public GlobalModelAdvice(ConstanteService constanteService, CarritoService carritoService) {
        this.constanteService = constanteService;
        this.carritoService = carritoService;
    }

    @ModelAttribute("tamMaxImagen")
    public long tamMaxImagen() {
        return Long.parseLong(constanteService.getValor("TAM_MAX_IMAGEN", "524288"));
    }

    // Expone el total del carrito a todas las paginas, para el badge del navbar (carrito/fragmentos :: verCarrito)
    @ModelAttribute("totalCarrito")
    public BigDecimal totalCarrito(HttpSession session) {
        return carritoService.getTotal(session);
    }
}
