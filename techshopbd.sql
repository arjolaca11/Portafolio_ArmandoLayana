create database if not exists techshop;
use techshop;

create table categoria (
    id_categoria INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(50) NOT NULL,
    ruta_imagen varchar(1024),
    activo boolean,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (id_categoria),
    unique (descripcion),
    index ndx_descripcion (descripcion)
) ENGINE = InnoDB;