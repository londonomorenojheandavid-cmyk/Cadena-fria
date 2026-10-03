"""Inventario de lotes (ECS-PRG-02) - HU-02 y HU-03."""
from __future__ import annotations

from datetime import datetime
from typing import Dict, List, Optional

from .domain import Lote


class Inventario:
    def __init__(self) -> None:
        self._lotes: Dict[str, Lote] = {}
        self._consecutivo = 0

    def registrar_ingreso(self, especie: str, peso_kg: float, cava_id: str,
                          temp_ingreso_c: float, ahora: Optional[datetime] = None) -> Lote:
        if peso_kg <= 0:
            raise ValueError("El peso debe ser mayor que cero")
        ahora = ahora or datetime.now()
        self._consecutivo += 1
        codigo = "L%s-%04d" % (ahora.strftime("%Y%m%d"), self._consecutivo)  # código autogenerado
        lote = Lote(codigo, especie, float(peso_kg), cava_id, temp_ingreso_c, ahora)
        self._lotes[codigo] = lote
        return lote

    def registrar_salida(self, codigo: str, peso_kg: float) -> Lote:
        lote = self._lotes[codigo]  # KeyError si el lote no existe
        if peso_kg <= 0:
            raise ValueError("El peso retirado debe ser mayor que cero")
        if peso_kg > lote.peso_kg:
            raise ValueError("No se puede retirar más de lo disponible")
        lote.peso_kg = round(lote.peso_kg - peso_kg, 3)
        return lote

    def buscar(self, codigo: str) -> Optional[Lote]:
        return self._lotes.get(codigo)

    def lotes_en_cava(self, cava_id: str) -> List[Lote]:
        return [l for l in self._lotes.values() if l.cava_id == cava_id and l.peso_kg > 0]
