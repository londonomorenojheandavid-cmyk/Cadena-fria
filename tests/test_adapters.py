import unittest

from cadenafria.adapters import TermometroMarcaA, TermometroMarcaB


class TestAdapters(unittest.TestCase):
    def test_marca_a_convierte_fahrenheit(self):
        lec = TermometroMarcaA().leer({"id": "C1", "temp_f": -0.4, "ts": "2026-10-02T10:00:00"})
        self.assertAlmostEqual(lec.temperatura_c, -18.0, places=1)

    def test_marca_b_convierte_decimas(self):
        lec = TermometroMarcaB().leer({"sensor": "C1", "decimas_c": -175, "ts": 1790000000})
        self.assertEqual(lec.temperatura_c, -17.5)
        self.assertTrue(lec.es_critica)


if __name__ == "__main__":
    unittest.main()
