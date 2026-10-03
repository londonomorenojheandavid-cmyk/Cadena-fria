# ECS-DOC-02 · Manual técnico (v1.0)

## Requisitos
Python 3.10+ (solo biblioteca estándar).

## Estructura
`src/cadenafria/` contiene el dominio, el inventario, los adaptadores de termómetros y los observadores de alerta.

## Pruebas
```bash
PYTHONPATH=src python -m unittest discover -s tests -v
```

## Esquema de datos
`data/schema.sql` (línea base). Los cambios posteriores se aplican con scripts versionados en `data/migrations/`.
