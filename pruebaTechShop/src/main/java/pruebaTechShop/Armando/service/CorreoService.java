package pruebaTechShop.Armando.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {

    private final JavaMailSender mailSender;

    public CorreoService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviar(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mailSender.send(mensaje);
    }
}
