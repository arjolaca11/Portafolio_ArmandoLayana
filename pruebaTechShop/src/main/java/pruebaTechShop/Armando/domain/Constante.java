package pruebaTechShop.Armando.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
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

    @Column(length = 25)
    private String atributo;

    @Column(length = 150)
    private String valor;
}
