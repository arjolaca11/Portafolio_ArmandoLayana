package pruebaTechShop.Armando.controller;

import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pruebaTechShop.Armando.domain.Factura;
import pruebaTechShop.Armando.domain.Usuario;
import pruebaTechShop.Armando.service.CarritoService;
import pruebaTechShop.Armando.service.FacturaService;
import pruebaTechShop.Armando.service.ProductoService;
import pruebaTechShop.Armando.service.UsuarioService;

@Controller
public class CarritoController {

    private final CarritoService carritoService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final FacturaService facturaService;
    private final MessageSource messageSource;

    public CarritoController(CarritoService carritoService, ProductoService productoService,
            UsuarioService usuarioService, FacturaService facturaService, MessageSource messageSource) {
        this.carritoService = carritoService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.facturaService = facturaService;
        this.messageSource = messageSource;
    }

    @GetMapping("/carrito/listado")
    public String listado(HttpSession session, Model model) {
        model.addAttribute("carritoItems", carritoService.obtenerCarrito(session));
        model.addAttribute("totalCarrito", carritoService.getTotal(session));
        return "carrito/listado";
    }

    // Llamado por AJAX (rutinas.js: addCart); solo devuelve el fragmento del boton/badge del carrito
    @PostMapping("/carrito/agregar")
    public String agregar(@RequestParam Integer idProducto, HttpSession session, Model model) {
        var producto = productoService.getProducto(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto con ID " + idProducto + " no encontrado."));
        carritoService.agregar(session, producto);
        model.addAttribute("totalCarrito", carritoService.getTotal(session));
        return "carrito/fragmentos :: verCarrito";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminar(@RequestParam Integer idProducto, HttpSession session) {
        carritoService.eliminar(session, idProducto);
        return "redirect:/carrito/listado";
    }

    @GetMapping("/carrito/modificar/{idProducto}")
    public String modificarForm(@PathVariable("idProducto") Integer idProducto, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        var itemOpt = carritoService.obtenerCarrito(session).stream()
                .filter(i -> i.getProducto().getIdProducto().equals(idProducto))
                .findFirst();
        if (itemOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("carrito.itemNoEncontrado", null, Locale.getDefault()));
            return "redirect:/carrito/listado";
        }
        model.addAttribute("item", itemOpt.get());
        return "carrito/modifica";
    }

    @PostMapping("/carrito/modificar")
    public String modificar(@RequestParam Integer idProducto, @RequestParam int cantidad, HttpSession session) {
        carritoService.actualizar(session, idProducto, cantidad);
        return "redirect:/carrito/listado";
    }

    // Fuera de "/carrito/**" a proposito: esta ruta exige estar autenticado (ver SecurityConfig).
    @PostMapping("/facturar/carrito")
    public String facturar(HttpSession session, Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.getUsuarioPorUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + principal.getName()));
        try {
            Factura factura = facturaService.facturar(session, usuario);
            return "redirect:/carrito/verFactura?idFactura=" + factura.getIdFactura();
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("carrito.vacio", null, Locale.getDefault()));
            return "redirect:/carrito/listado";
        }
    }

    @GetMapping("/carrito/verFactura")
    public String verFactura(@RequestParam Integer idFactura, Model model) {
        model.addAttribute("factura", facturaService.getFacturaConVentas(idFactura));
        return "carrito/verFactura";
    }
}
