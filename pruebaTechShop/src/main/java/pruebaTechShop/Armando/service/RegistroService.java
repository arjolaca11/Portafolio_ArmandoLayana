package pruebaTechShop.Armando.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import pruebaTechShop.Armando.domain.Constante;
import pruebaTechShop.Armando.domain.Usuario;

@Service
public class RegistroService {

    private final CorreoService correoService;
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;
    private final ConstanteService constanteService;
    private final PasswordEncoder passwordEncoder;
    private final String servidor;

    public RegistroService(CorreoService correoService, UsuarioService usuarioService, MessageSource messageSource,
            ConstanteService constanteService, PasswordEncoder passwordEncoder) {
        this.correoService = correoService;
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
        this.constanteService = constanteService;
        this.passwordEncoder = passwordEncoder;
        // Ojo como se lee una informacion del application.properties... pero desde la tabla "constante"
        Optional<Constante> constante = constanteService.findByAtributo("servidor.http");
        servidor = constante.isPresent() ? constante.get().getValor() : "localhost";
    }

    // Crea la cuenta (inactiva) y envia el correo con el enlace de activacion
    @Transactional
    public void registrar(Usuario usuario, String passwordPlano) {
        usuario.setPassword(passwordEncoder.encode(passwordPlano));
        usuario.setActivo(false);
        usuario.setRoles(new HashSet<>());
        String token = UUID.randomUUID().toString();
        usuario.setClaveActivacion(token);
        usuario = usuarioService.save(usuario);
        usuarioService.asignarRol(usuario.getUsername(), "USER");

        String enlace = servidor + "/registro/activar?username=" + usuario.getUsername() + "&clave=" + token;
        String asunto = messageSource.getMessage("registro.correoAsunto", null, Locale.getDefault());
        String cuerpo = messageSource.getMessage("registro.correoCuerpo", new Object[]{usuario.getUsername(), enlace}, Locale.getDefault());
        correoService.enviar(usuario.getCorreo(), asunto, cuerpo);
    }

    // Este metodo se usa en el enlace del correo enviado...
    public Model activar(Model model, String username, String clave) {
        Optional<Usuario> usuario = usuarioService.getUsuarioPorUsernameYClaveActivacion(username, clave);
        if (!usuario.isEmpty()) { //Si estaba...
            Usuario u = usuario.get();
            u.setActivo(true);
            u.setClaveActivacion(null);
            usuarioService.save(u);
            model.addAttribute("usuario", u);
        } else { //hay que devolver error
            model.addAttribute("titulo", messageSource.getMessage("registro.activar", null, Locale.getDefault()));
            model.addAttribute("mensaje", messageSource.getMessage("registro.activar.error", null, Locale.getDefault()));
        }
        return model;
    }
}
