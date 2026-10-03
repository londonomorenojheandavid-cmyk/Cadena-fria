"""Modelo de dominio (ECS-PRG-01)."""
from __future__ import annotations

from dataclasses import dataclass
from datetime import datetime

# Requisitos del caso (Parcial I)
UMBRAL_CRITICO_C = -18.0     # alerta crítica si la temperatura SUPERA este valor
TIEMPO_MAX_ALERTA_S = 30     # RNF: la alerta debe emitirse en <= 30 s
# Parámetro configurable (supuesto del proyecto): banda de vigilancia (ámbar)
UMBRAL_VIGILANCIA_C = -20.0


def estado_por_temperatura(temperatura_c: float) -> str:
    """Devuelve 'critico', 'vigilancia' o 'normal' (rojo / ámbar / verde en la UI)."""
    if temperatura_c > UMBRAL_CRITICO_C:
        return "critico"
    if temperatura_c > UMBRAL_VIGILANCIA_C:
        return "vigilancia"
    return "normal"


@dataclass(frozen=True)
class Lectura:
    cava_id: str
    temperatura_c: float
    instante: datetime
    fuente: str = "desconocida"

    @property
    def estado(self) -> str:
        return estado_por_temperatura(self.temperatura_c)

    @property
    def es_critica(self) -> bool:
        return self.estado == "critico"


@dataclass
class Lote:
    codigo: str
    especie: str
    peso_kg: float
    cava_id: str
    temp_ingreso_c: float
    ingreso: datetime
