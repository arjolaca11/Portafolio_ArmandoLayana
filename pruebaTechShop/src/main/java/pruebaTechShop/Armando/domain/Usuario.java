package pruebaTechShop.Armando.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// mapea la tabla "usuario" (ver creaTablas.sql)
@Data
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    // longitudes alineadas a la tabla "usuario" real en BD
    @Column(unique = true, nullable = false, length = 30)
    @NotBlank
    @Size(max = 30)
    private String username;

    @Column(nullable = false, length = 512)
    @NotBlank
    private String password;

    @Column(length = 20)
    @Size(max = 20)
    private String nombre;

    @Column(length = 30)
    @Size(max = 30)
    private String apellidos;

    @Column(unique = true, length = 75)
    @Size(max = 75)
    private String correo;

    @Column(length = 25)
    private String telefono;

    @Column(name = "ruta_imagen", length = 1024)
    private String rutaImagen;

    private Boolean activo;

    // token de un solo uso enviado por correo; se limpia una vez que el usuario activa la cuenta
    @Column(name = "clave_activacion", length = 100)
    private String claveActivacion;

    // Relacion muchos a muchos con Rol, a traves de la tabla intermedia usuario_rol.
    // Se excluye de toString/equals/hashCode por seguridad (evita loguear password/roles juntos
    // y evita recursion si Rol llegara a referenciar de vuelta a Usuario).
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<Rol> roles = new HashSet<>();
}
