package pruebaTechShop.Armando.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pruebaTechShop.Armando.domain.Rol;
import pruebaTechShop.Armando.domain.Usuario;
import pruebaTechShop.Armando.repository.RolRepository;
import pruebaTechShop.Armando.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Usado por el enlace de activacion enviado por correo (ver RegistroService.activar)
    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorUsernameYClaveActivacion(String username, String claveActivacion) {
        return usuarioRepository.findByUsername(username)
                .filter(u -> claveActivacion != null && claveActivacion.equals(u.getClaveActivacion()));
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Seccion para gestionar roles a usuarios...

    @Transactional(readOnly = true)
    public List<String> getRolesNombres() {
        // Retorna una lista de Strings con el nombre de cada rol
        return rolRepository.findAll().stream()
                .map(Rol::getRol)
                .toList();
    }

    @Transactional
    public Usuario asignarRol(String username, String nombreRol) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + username));
        Rol rol = rolRepository.findByRol(nombreRol)
                .orElseThrow(() -> new NoSuchElementException("Rol no encontrado: " + nombreRol));
        usuario.getRoles().add(rol);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario eliminarRol(String username, Integer idRol) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado: " + username);
        }
        Usuario usuario = usuarioOpt.get();

        // Filtra la coleccion de roles del usuario para mantener solo los que NO coinciden con idRol
        usuario.getRoles().removeIf(rol -> rol.getIdRol().equals(idRol));

        // Guarda el usuario con la coleccion de roles modificada
        return usuarioRepository.save(usuario);
    }
}
