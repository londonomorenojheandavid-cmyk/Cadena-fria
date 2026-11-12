CREATE DATABASE IF NOT EXISTS friopesca
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 2. Base de datos para Fases I y II (Hibernate ORM - CadenaFría Urabá)
CREATE DATABASE IF NOT EXISTS cadena_frio
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE cadena_frio;

CREATE TABLE IF NOT EXISTS cooperativa (
    id_cooperativa INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    municipio VARCHAR(60) NOT NULL,
    telefono VARCHAR(20)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_cooperativa INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'OPERARIO', 'COMERCIANTE') NOT NULL,
    CONSTRAINT fk_usuario_cooperativa
        FOREIGN KEY (id_cooperativa) REFERENCES cooperativa(id_cooperativa)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS cava (
    id_cava INT AUTO_INCREMENT PRIMARY KEY,
    id_cooperativa INT NOT NULL,
    nombre VARCHAR(60) NOT NULL,
    capacidad_kg DECIMAL(10, 2) NOT NULL,
    temp_critica DECIMAL(5, 2) NOT NULL DEFAULT -18.00,
    CONSTRAINT fk_cava_cooperativa
        FOREIGN KEY (id_cooperativa) REFERENCES cooperativa(id_cooperativa)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    especie VARCHAR(80) NOT NULL,
    presentacion VARCHAR(40) NOT NULL,
    unidad VARCHAR(10) NOT NULL DEFAULT 'KG',
    precio_kg DECIMAL(10, 2) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS lote_inventario (
    id_lote INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_cava INT NOT NULL,
    id_usuario INT NOT NULL,
    cantidad_kg DECIMAL(10, 2) NOT NULL,
    fecha_ingreso DATETIME NOT NULL,
    fecha_vencimiento DATE,
    estado ENUM('DISPONIBLE', 'AGOTADO', 'DESCARTADO') NOT NULL DEFAULT 'DISPONIBLE',
    CONSTRAINT fk_lote_producto
        FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    CONSTRAINT fk_lote_cava
        FOREIGN KEY (id_cava) REFERENCES cava(id_cava),
    CONSTRAINT fk_lote_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS lectura_temperatura (
    id_lectura BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cava INT NOT NULL,
    temperatura DECIMAL(5, 2) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    CONSTRAINT fk_lectura_cava
        FOREIGN KEY (id_cava) REFERENCES cava(id_cava)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS alerta (
    id_alerta INT AUTO_INCREMENT PRIMARY KEY,
    id_lectura BIGINT NOT NULL,
    id_cava INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    atendida TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_alerta_lectura
        FOREIGN KEY (id_lectura) REFERENCES lectura_temperatura(id_lectura),
    CONSTRAINT fk_alerta_cava
        FOREIGN KEY (id_cava) REFERENCES cava(id_cava)
) ENGINE=InnoDB;
