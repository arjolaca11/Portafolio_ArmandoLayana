package pruebaTechShop.Armando.controller;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pruebaTechShop.Armando.domain.Usuario;
import pruebaTechShop.Armando.service.RegistroService;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    private final RegistroService registroService;
    private final MessageSource messageSource;

    public RegistroController(RegistroService registroService, MessageSource messageSource) {
        this.registroService = registroService;
        this.messageSource = messageSource;
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro/nuevo";
    }

    @PostMapping("/guardar")
    public String guardar(Usuario usuario, @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            registroService.registrar(usuario, password);
            redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("registro.exito", null, Locale.getDefault()));
            return "redirect:/login";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("registro.errorDuplicado", null, Locale.getDefault()));
            return "redirect:/registro/nuevo";
        } catch (MailException e) {
            // El SMTP no esta configurado (o fallo la autenticacion); la cuenta no se crea (transaccion revertida)
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("registro.errorCorreo", null, Locale.getDefault()));
            return "redirect:/registro/nuevo";
        }
    }

    @GetMapping("/activar")
    public String activar(@RequestParam String username, @RequestParam String clave, Model model) {
        registroService.activar(model, username, clave);
        return "registro/activado";
    }
}
