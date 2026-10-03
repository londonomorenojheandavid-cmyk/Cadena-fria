import unittest
from datetime import datetime

from cadenafria.inventario import Inventario


class TestInventario(unittest.TestCase):
    def setUp(self):
        self.inv = Inventario()
        self.t = datetime(2026, 10, 2, 8, 0)

    def test_ingreso_genera_codigo_automatico(self):
        lote = self.inv.registrar_ingreso("Pargo", 40, "C1", -19.5, self.t)
        self.assertEqual(lote.codigo, "L20261002-0001")

    def test_salida_no_puede_superar_disponible(self):
        lote = self.inv.registrar_ingreso("Pargo", 40, "C1", -19.5, self.t)
        with self.assertRaises(ValueError):
            self.inv.registrar_salida(lote.codigo, 41)

    def test_salida_actualiza_existencias(self):
        lote = self.inv.registrar_ingreso("Pargo", 40, "C1", -19.5, self.t)
        self.inv.registrar_salida(lote.codigo, 15)
        self.assertEqual(self.inv.buscar(lote.codigo).peso_kg, 25)


if __name__ == "__main__":
    unittest.main()
