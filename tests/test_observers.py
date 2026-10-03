import unittest
from datetime import datetime

from cadenafria.domain import Lectura
from cadenafria.observers import (AlarmaAudible, CavaAlmacenamiento, NotificadorSMS,
                                  PanelDashboard, RegistroEventos)


class Roto(NotificadorSMS):
    def __init__(self):
        super().__init__(lambda m: None)

    def notificar(self, evento):
        raise RuntimeError("sin cobertura")


class TestObservers(unittest.TestCase):
    def setUp(self):
        self.t = datetime(2026, 10, 2, 10, 0)

    def test_lectura_normal_no_genera_evento(self):
        cava = CavaAlmacenamiento("C1")
        self.assertIsNone(cava.procesar(Lectura("C1", -21.0, self.t)))

    def test_lectura_critica_notifica_a_todos_los_canales(self):
        sms = []
        alarma, panel, reg = AlarmaAudible(), PanelDashboard(), RegistroEventos()
        cava = CavaAlmacenamiento("C1")
        for o in (alarma, NotificadorSMS(sms.append), panel, reg):
            cava.suscribir(o)
        cava.procesar(Lectura("C1", -15.0, self.t))
        self.assertEqual(alarma.sonando, ["C1"])
        self.assertEqual(len(sms), 1)
        self.assertEqual(panel.tarjetas["C1"][0], "critico")
        self.assertEqual(len(reg.eventos), 1)

    def test_falla_de_un_canal_no_bloquea_los_demas(self):
        reg = RegistroEventos()
        cava = CavaAlmacenamiento("C1")
        cava.suscribir(Roto())
        cava.suscribir(reg)
        cava.procesar(Lectura("C1", -10.0, self.t))
        self.assertEqual(len(reg.eventos), 1)
        self.assertEqual(cava.fallos[0][0], "Roto")


if __name__ == "__main__":
    unittest.main()
