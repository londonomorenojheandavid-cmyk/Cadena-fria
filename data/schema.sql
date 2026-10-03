-- ECS-DAT-01 · Esquema de datos línea base LB-1 (v1.0.0)
CREATE TABLE cava (
  cava_id       TEXT PRIMARY KEY,
  nombre        TEXT NOT NULL,
  cooperativa   TEXT NOT NULL
);

CREATE TABLE lote (
  codigo          TEXT PRIMARY KEY,           -- autogenerado: LAAAAMMDD-NNNN
  especie         TEXT NOT NULL,
  peso_kg         REAL NOT NULL CHECK (peso_kg >= 0),
  cava_id         TEXT NOT NULL REFERENCES cava(cava_id),
  temp_ingreso_c  REAL NOT NULL,
  ingreso         TIMESTAMP NOT NULL
);

CREATE TABLE lectura (
  id            INTEGER PRIMARY KEY AUTOINCREMENT,
  cava_id       TEXT NOT NULL REFERENCES cava(cava_id),
  temperatura_c REAL NOT NULL,
  instante      TIMESTAMP NOT NULL,
  fuente        TEXT NOT NULL
);

CREATE TABLE evento_alerta (
  id            INTEGER PRIMARY KEY AUTOINCREMENT,
  cava_id       TEXT NOT NULL REFERENCES cava(cava_id),
  temperatura_c REAL NOT NULL,
  instante      TIMESTAMP NOT NULL,
  estado        TEXT NOT NULL CHECK (estado IN ('normal','vigilancia','critico'))
);
