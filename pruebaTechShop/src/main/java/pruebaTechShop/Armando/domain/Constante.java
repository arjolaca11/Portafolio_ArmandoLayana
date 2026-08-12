package pruebaTechShop.Armando.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

// mapea la tabla "constante": valores de configuracion del sistema (ver creaTablas.sql)
@Data
@Entity
@Table(name = "constante")
public class Constante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_constante")
    private Integer idConstante;

    @Column(name = "atributo", unique = true, nullable = false, length = 25)
    @NotBlank
    @Size(max = 25)
    private String atributo;

    @Column(name = "valor", nullable = false, length = 150)
    @NotBlank
    @Size(max = 150)
    private String valor;

    // Ambas fechas las gestiona la BD (DEFAULT CURRENT_TIMESTAMP / ON UPDATE CURRENT_TIMESTAMP);
    // se marcan no-insertable/no-actualizable para que Hibernate nunca las sobreescriba con null.
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;
}
