package pruebaTechShop.Armando;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import pruebaTechShop.Armando.service.UsuarioDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Rutas de acceso libre. Cualquier persona (autenticada o no) puede entrar.
    public static final String[] PUBLIC_URLS = {
        "/", "/index", "/ejemplo2", "/multimedia", "/iframes",
        "/consultas/**", "/carrito/**", "/registro/**",
        "/js/**", "/css/**", "/webjars/**",
        "/login", "/acceso_denegado"
    };

    // Rutas que requieren estar autenticado (cualquier rol), pero no un rol especifico: el checkout
    // del carrito. Vive fuera de "/carrito/**" (que es publico) para poder exigir login aqui.
    public static final String[] USUARIO_URLS = {
        "/facturar/carrito"
    };

    // Rutas que pueden ser accedidas por los roles ADMIN y VENDEDOR (solo lectura)
    public static final String[] ADMIN_OR_VENDEDOR_URLS = {
        "/producto/listado", "/categoria/listado"
    };

    // Rutas exclusivas para el rol ADMIN (crear, modificar, eliminar; gestion de constantes y roles)
    public static final String[] ADMIN_URLS = {
        "/producto/**", "/categoria/**", "/constante/**", "/usuario_rol/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) throws Exception {
        http.authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(USUARIO_URLS).authenticated()
                .requestMatchers(ADMIN_OR_VENDEDOR_URLS).hasAnyRole("ADMIN", "VENDEDOR")
                .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                .anyRequest().authenticated()
        ).formLogin(form -> form // Configuracion de formulario de login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        ).logout(logout -> logout // Configuracion de logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).exceptionHandling(exceptions -> exceptions // Manejo de excepciones
                .accessDeniedPage("/acceso_denegado")
        ).sessionManagement(session -> session // Configuracion de sesiones
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );
        return http.build();
    }

    // necesario para que maximumSessions() detecte correctamente cuando una sesion termina
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Autentica contra la tabla "usuario" (ver UsuarioDetailsService), reemplazando
    // los usuarios en memoria de semana 9.
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UsuarioDetailsService usuarioDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(usuarioDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
