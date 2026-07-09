package pruebaTechShop.Armando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityViewController {

    // Sin restriccion de metodo HTTP: el AccessDeniedHandler de Spring Security
    // reenvia (forward) aqui tanto peticiones GET como POST/PUT/DELETE que fueron
    // rechazadas (por rol insuficiente o por token CSRF invalido/ausente).
    @RequestMapping("/acceso_denegado")
    public String accesoDenegado() {
        return "acceso_denegado";
    }
}
