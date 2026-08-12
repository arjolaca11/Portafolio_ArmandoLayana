package pruebaTechShop.Armando.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pruebaTechShop.Armando.domain.Usuario;
import pruebaTechShop.Armando.service.UsuarioService;

@Controller
@RequestMapping("/usuario_rol")
public class UsuarioRolController {

    private final UsuarioService usuarioService;

    public UsuarioRolController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 1. Endpoint para la vista inicial (sin usuario)
    @GetMapping("/mantenimiento")
    public String mantenimiento(Model model) {
        model.addAttribute("usuario", new Usuario());
        // Se inicializan listas vacias para evitar errores de Thymeleaf
        model.addAttribute("rolesAsignados", Collections.emptySet());
        model.addAttribute("rolesDisponibles", Collections.emptyList());
        return "usuario_rol/mantenimiento";
    }

    // 2. Endpoint para buscar y mostrar roles
    @GetMapping("/buscar")
    public String buscarUsuario(@RequestParam("username") String username, Model model) {
        Usuario usuario = usuarioService.getUsuarioPorUsername(username).orElse(null);
        model.addAttribute("usuario", usuario);

        if (usuario != null) {
            List<String> todosRolesNombres = usuarioService.getRolesNombres();

            // Filtra los roles disponibles (los que no tiene el usuario)
            List<String> rolesDisponibles = todosRolesNombres.stream()
                    .filter(rolNombre -> usuario.getRoles().stream()
                            .noneMatch(rolAsignado -> rolAsignado.getRol().equals(rolNombre)))
                    .toList();

            model.addAttribute("rolesAsignados", usuario.getRoles());
            model.addAttribute("rolesDisponibles", rolesDisponibles);
        } else {
            model.addAttribute("rolesAsignados", Collections.emptySet());
            model.addAttribute("rolesDisponibles", Collections.emptyList());
        }
        return "usuario_rol/mantenimiento";
    }

    // 3. Endpoint para asignar un rol
    @PostMapping("/asignar")
    public String asignarRol(@RequestParam String username, @RequestParam String rol) {
        usuarioService.asignarRol(username, rol);
        return "redirect:/usuario_rol/buscar?username=" + username;
    }

    // 4. Endpoint para eliminar un rol
    @PostMapping("/eliminar")
    public String eliminarRol(@RequestParam String username, @RequestParam Integer idRol) {
        usuarioService.eliminarRol(username, idRol);
        return "redirect:/usuario_rol/buscar?username=" + username;
    }
}
