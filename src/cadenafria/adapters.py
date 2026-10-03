"""Patrón Adapter (ECS-PRG-03) - HU-07: termómetros de distintas marcas."""
from __future__ import annotations

from abc import ABC, abstractmethod
from datetime import datetime, timezone

from .domain import Lectura


class TermometroAdapter(ABC):
    """Interfaz común esperada por el sistema. Agregar una marca = agregar una subclase."""

    @abstractmethod
    def leer(self, crudo: dict) -> Lectura: ...


class TermometroMarcaA(TermometroAdapter):
    """Marca A: {"id": "C1", "temp_f": -1.0, "ts": "2026-10-02T10:00:00"} (Fahrenheit, ISO-8601)."""

    def leer(self, crudo: dict) -> Lectura:
        celsius = (float(crudo["temp_f"]) - 32.0) * 5.0 / 9.0
        return Lectura(crudo["id"], round(celsius, 2), datetime.fromisoformat(crudo["ts"]), "marca-a")


class TermometroMarcaB(TermometroAdapter):
    """Marca B: {"sensor": "C1", "decimas_c": -175, "ts": 1790000000} (décimas de °C, epoch)."""

    def leer(self, crudo: dict) -> Lectura:
        celsius = int(crudo["decimas_c"]) / 10.0
        instante = datetime.fromtimestamp(int(crudo["ts"]), tz=timezone.utc).replace(tzinfo=None)
        return Lectura(crudo["sensor"], celsius, instante, "marca-b")
