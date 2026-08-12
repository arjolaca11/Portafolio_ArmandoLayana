package pruebaTechShop.Armando.service;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pruebaTechShop.Armando.domain.Usuario;
import pruebaTechShop.Armando.repository.UsuarioRepository;

// Reemplaza a los usuarios en memoria de SecurityConfig: Spring Security llama a este
// servicio en cada login para autenticar contra la tabla "usuario".
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getRol()))
                .toList();

        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(usuario.getActivo() != null && !usuario.getActivo())
                .authorities(authorities)
                .build();
    }
}
