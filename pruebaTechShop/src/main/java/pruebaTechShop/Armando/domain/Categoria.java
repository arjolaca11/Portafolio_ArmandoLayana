package pruebaTechShop.Armando.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// mapea la tabla "categoria" (ver creaTablas.sql)
@Data
@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(unique = true, nullable = false, length = 50)
    @NotNull
    @Size(max = 50)
    private String descripcion;

    @Column(name = "ruta_imagen", length = 1024)
    @Size(max = 1024)
    private String rutaImagen;

    @Column(name = "activo")
    private Boolean activo;

    // Relacion de uno a muchos con la clase Producto.
    // Se excluye de toString/equals/hashCode para evitar recursion infinita
    // con el lado @ManyToOne de Producto (ver Producto.categoria).
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos;
}
