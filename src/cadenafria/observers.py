"""Patrón Observer + CavaAlmacenamiento (ECS-PRG-04) - HU-01, HU-06, HU-08."""
from __future__ import annotations

from abc import ABC, abstractmethod
from dataclasses import dataclass
from datetime import datetime
from typing import Callable, List, Optional, Tuple

from .domain import Lectura


@dataclass(frozen=True)
class EventoAlerta:
    cava_id: str
    temperatura_c: float
    instante: datetime
    estado: str


class Observador(ABC):
    @abstractmethod
    def notificar(self, evento: EventoAlerta) -> None: ...


class AlarmaAudible(Observador):
    def __init__(self) -> None:
        self.sonando: List[str] = []

    def notificar(self, evento: EventoAlerta) -> None:
        self.sonando.append(evento.cava_id)


class NotificadorSMS(Observador):
    def __init__(self, enviar: Callable[[str], None]) -> None:
        self._enviar = enviar

    def notificar(self, evento: EventoAlerta) -> None:
        self._enviar("ALERTA cava %s: %.1f C (%s)" % (evento.cava_id, evento.temperatura_c, evento.estado))


class PanelDashboard(Observador):
    def __init__(self) -> None:
        self.tarjetas = {}

    def notificar(self, evento: EventoAlerta) -> None:
        self.tarjetas[evento.cava_id] = (evento.estado, evento.temperatura_c)


class RegistroEventos(Observador):
    """Historial de alertas (HU-06)."""

    def __init__(self) -> None:
        self.eventos: List[EventoAlerta] = []

    def notificar(self, evento: EventoAlerta) -> None:
        self.eventos.append(evento)


class CavaAlmacenamiento:
    def __init__(self, cava_id: str) -> None:
        self.cava_id = cava_id
        self._observadores: List[Observador] = []
        self.fallos: List[Tuple[str, str]] = []   # (observador, error) - HU-08

    def suscribir(self, observador: Observador) -> None:
        self._observadores.append(observador)

    def procesar(self, lectura: Lectura) -> Optional[EventoAlerta]:
        """Publica un evento si la lectura es crítica. Un observador que falla no bloquea a los demás."""
        if not lectura.es_critica:
            return None
        evento = EventoAlerta(self.cava_id, lectura.temperatura_c, lectura.instante, lectura.estado)
        for obs in self._observadores:
            try:
                obs.notificar(evento)
            except Exception as exc:  # noqa: BLE001 - se registra y se continúa
                self.fallos.append((type(obs).__name__, str(exc)))
        return evento
