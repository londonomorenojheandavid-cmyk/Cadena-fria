# ECS-DOC-01 · Especificación arquitectónica (resumen)

Fuente: Parcial I (prototipo UI/UX y estrategia ágil con Scrum).

* **Estilo:** arquitectura orientada a eventos. Cada cava publica lecturas de temperatura como eventos y el sistema reacciona.
* **Patrón Adapter** (`adapters.py`): aísla las diferencias entre marcas de termómetros (HU-07).
* **Patrón Observer** (`observers.py`): `CavaAlmacenamiento` notifica a `AlarmaAudible`, `NotificadorSMS`, `PanelDashboard` y `RegistroEventos` (HU-01, HU-06).
* **Resiliencia** (HU-08): el fallo de un observador no bloquea a los demás.
* **Requisitos no funcionales:** umbral crítico > -18 °C; alerta crítica en ≤ 30 s; operación con humedad salina y cortes eléctricos.

```mermaid
flowchart LR
  T1[Termómetro marca A] -->|dato crudo| A1[Adapter A]
  T2[Termómetro marca B] -->|dato crudo| A2[Adapter B]
  A1 --> L[Lectura]
  A2 --> L
  L --> C[CavaAlmacenamiento]
  C -->|evento de alerta| O1[AlarmaAudible]
  C --> O2[NotificadorSMS]
  C --> O3[PanelDashboard]
  C --> O4[RegistroEventos]
```
