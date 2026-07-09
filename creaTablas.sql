create table categoria (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(50) NOT NULL,
  ruta_imagen varchar(1024),
  activo boolean,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_categoria),
  unique (descripcion),
  index ndx_descripcion (descripcion))
  ENGINE = InnoDB;

create table producto (
  id_producto INT NOT NULL AUTO_INCREMENT,
  id_categoria INT NOT NULL,
  descripcion VARCHAR(50) NOT NULL,
  detalle TEXT,
  precio DECIMAL(12,2) NOT NULL,
  existencias INT NOT NULL DEFAULT 0,
  ruta_imagen VARCHAR(1024),
  activo boolean,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_producto),
  index ndx_descripcion (descripcion),
  CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria))
  ENGINE = InnoDB;

-- tabla de valores de configuracion (parametros del sistema), ver reto sem05
create table constante (
  id_constante INT NOT NULL AUTO_INCREMENT,
  atributo VARCHAR(25) NOT NULL,
  valor VARCHAR(150) NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_constante),
  unique (atributo))
  ENGINE = InnoDB;

insert into constante (atributo, valor) values ('TAM_MAX_IMAGEN', '524288');