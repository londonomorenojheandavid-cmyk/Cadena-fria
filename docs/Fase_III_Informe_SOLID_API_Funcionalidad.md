# Entrega Fase III (ING-PROY-4): Principios SOLID, Consumo de API Externa e Implementación Completa

* **Proyecto:** Sistema de inventario y cadena de frío para comerciantes de pescado (FríoPesca / CadenaFría Urabá)
* **Estudiante:** Jean David Londoño Moreno
* **Asignatura:** Lenguaje de Programación III
* **Docente:** Marta Lucía Jiménez Torres
* **Institución:** Corporación Universitaria Remington — Uniremington (Facultad de Ingenierías)
* **Fecha:** Noviembre de 2026

---

## 1. Descripción de la funcionalidad completa del proyecto asignado

**FríoPesca (CadenaFría Urabá)** es una aplicación web desarrollada en **Java** con persistencia **Hibernate ORM / Spring Data JPA** sobre **MySQL 8**, diseñada para comerciantes de pescado y cooperativas pesqueras de la región de Urabá (Necoclí y Turbo).

El sistema cubre el ciclo operativo completo del negocio pesquero:

1. **Autenticación y Seguridad (`SecurityConfig.java`, `/login`):**
   Control de acceso administrativo con credenciales configurables y contraseñas cifradas con `BCryptPasswordEncoder`.
2. **Panel de Control (`/`, `inicio.html`):**
   Presenta indicadores en tiempo real del total de productos registrados, lotes activos, histórico de lecturas con alerta térmica y movimientos de inventario, junto con las condiciones meteorológicas exteriores de la zona consultadas desde la API de **Open-Meteo**.
3. **Gestión del Catálogo de Productos (`/productos`, `Producto.java`):**
   Permite crear productos pesqueros indicando nombre comercial (ej. *Bocachico*, *Pargo Rojo*, *Tilapia*), especie científica y precio por kilogramo en pesos colombianos (COP). Incluye desactivación lógica para conservar la trazabilidad histórica de lotes anteriores sin borrar registros físicamente.
4. **Gestión de Inventario por Lotes (`/lotes`, `Lote.java`, `LoteInventario.java`):**
   Permite registrar el ingreso de lotes asociándolos a un producto activo, código único (ej. `LOT-001`), kilos iniciales, fecha de ingreso, fecha de vencimiento, ubicación física (`Nevera 1` / `Cava 1`) y rango térmico permitido (`temperaturaMin` y `temperaturaMax` en °C). Al crear el lote, se registra automáticamente un movimiento inicial de tipo `ENTRADA`.
5. **Control de Cadena de Frío y Alertas (`/temperaturas`, `LecturaTemperatura.java`, `Alerta.java`):**
   Permite registrar lecturas de temperatura con responsable y observación. El servicio evalúa automáticamente si la temperatura medida se encuentra fuera del rango configurado para el lote (o si supera el umbral crítico de `-18.00 °C` en cavas de congelación), marcando la lectura como **`FUERA DE RANGO`** y activando una alerta visual inmediata.
6. **Gestión de Ventas y Mermas (`/movimientos`, `Movimiento.java`):**
   Registra salidas de inventario por `VENTA` o `MERMA`. El sistema valida que existan kilogramos disponibles suficientes (rechazando salidas que superen el stock con el mensaje *"Inventario insuficiente"*), impide vender lotes vencidos, descuenta automáticamente el saldo en kilogramos del lote y calcula el valor total en COP (`kilos * precioKg`).
7. **Reportes Operativos (`/reportes`, `reportes.html`):**
   Consolida en una sola vista las existencias actuales por lote, el historial de movimientos (entradas, ventas y mermas) y la bitácora de temperaturas y alertas, con opción de exportación directa a **PDF** mediante impresión del navegador.

---

## 2. Principios SOLID implementados

En el diseño e implementación del proyecto se aplicaron explícitamente dos principios **SOLID**:

### 2.1. Principio de Responsabilidad Única (SRP — *Single Responsibility Principle*)
> *"Una clase debe tener una única razón para cambiar."*

**Cómo y por qué se implementó:**
En lugar de concentrar consultas SQL, validaciones de negocio, llamadas HTTP a la API del clima y respuestas HTML dentro de una sola clase, el sistema se dividió en capas con responsabilidades estrictamente separadas:

* **Capa de Presentación (`WebController.java` / `DashboardServlet.java`):** Su única responsabilidad es recibir las peticiones HTTP del usuario, delegar la operación al servicio correspondiente y retornar la vista Thymeleaf/JSP con los mensajes de confirmación o error.
* **Capa de Lógica de Negocio (`InventarioService.java` y `AlertaService.java`):** Su única responsabilidad es hacer cumplir las reglas del dominio pesquero (validar que los kilos sean positivos, que no se venda más pescado del disponible en el lote, que la fecha de vencimiento no sea anterior al ingreso y determinar si una lectura de temperatura genera alerta por romper la cadena de frío).
* **Capa de Persistencia y Acceso a Datos (`GenericDAOImpl.java`, `ProductoRepository.java`, `LoteRepository.java`, `MovimientoRepository.java`, `LecturaTemperaturaRepository.java`):** Su única responsabilidad es realizar las operaciones CRUD (`persist`/`save`, `find`, `merge`, `remove`) sobre MySQL a través de Hibernate ORM.
* **Capa de Integración Externa (`ClimaService.java` / `ClimaApiClient.java`):** Su única responsabilidad es conectarse vía HTTP a la API de Open-Meteo y transformar el JSON recibido.

**Beneficio obtenido:** Si cambia la fórmula de alerta térmica o las reglas de venta, solo se modifica `InventarioService`; si cambia la interfaz gráfica, solo se modifican las plantillas HTML; si cambia la API meteorológica, solo se modifica `ClimaService`.

---

### 2.2. Principio de Inversión de Dependencias (DIP — *Dependency Inversion Principle*)
> *"Los módulos de alto nivel no deben depender de módulos de bajo nivel; ambos deben depender de abstracciones."*

**Cómo y por qué se implementó:**
1. En la capa de persistencia de la Fase II, se definió la interfaz genérica `GenericDAO<T, ID>` como abstracción de acceso a datos, implementada por `GenericDAOImpl<T, ID>`. En la Fase III, `InventarioService` y `WebController` no instancian clases concretas con `new` ni manejan conexiones JDBC directas, sino que reciben las interfaces `ProductoRepository`, `LoteRepository`, `MovimientoRepository`, `LecturaTemperaturaRepository` y `ProveedorClima` mediante **inyección de dependencias por constructor**:

```java
@Service
public class InventarioService {
    private final LoteRepository lotes;
    private final MovimientoRepository movimientos;
    private final LecturaTemperaturaRepository lecturas;

    // Inyección de dependencias por constructor sobre abstracciones (DIP)
    public InventarioService(
            LoteRepository lotes,
            MovimientoRepository movimientos,
            LecturaTemperaturaRepository lecturas) {
        this.lotes = lotes;
        this.movimientos = movimientos;
        this.lecturas = lecturas;
    }
}
```

2. Asimismo, para el consumo del clima se definió la interfaz `ProveedorClima`, implementada por `ClimaService`. Así, el controlador depende de la abstracción y no de los detalles de implementación del cliente HTTP.

**Beneficio obtenido:** Permite cambiar el motor de base de datos (por ejemplo, de MySQL en producción a H2 en memoria durante las pruebas automatizadas con `InventarioServiceTest`) o cambiar el proveedor meteorológico sin modificar una sola línea de la lógica de negocio.

---

## 3. API externa utilizada, propósito y cómo se consume

### 3.1. API seleccionada: Open-Meteo (`https://api.open-meteo.com`)
Se seleccionó **Open-Meteo Weather Forecast API**, una API REST pública, gratuita y de libre acceso (no requiere API Key) que provee datos meteorológicos en tiempo real en formato **JSON**.

### 3.2. Propósito en el proyecto
En los municipios costeros de **Turbo y Necoclí (Urabá)**, las altas temperaturas ambientales (frecuentemente superiores a 28 °C – 32 °C) y la alta humedad relativa aceleran el riesgo de ruptura de la cadena de frío durante la recepción, apertura de cavas y despacho de pescado. Mostrar la **temperatura ambiental exterior** y el **porcentaje de humedad** en el Panel de Control permite al administrador correlacionar las condiciones climáticas externas con la frecuencia de revisión de las cavas y refrigeradores.

### 3.3. Cómo se consume en el código Java
La clase `ClimaService` realiza una petición HTTP `GET` mediante `RestClient` de Spring hacia el endpoint `/v1/forecast` enviando las coordenadas de Urabá (`latitude=7.88`, `longitude=-76.72`) y solicitando las variables `temperature_2m` y `relative_humidity_2m` en formato JSON:

```java
Map<String, Object> data = client.get()
        .uri(b -> b.path("/v1/forecast")
                .queryParam("latitude", lat)
                .queryParam("longitude", lon)
                .queryParam("current", "temperature_2m,relative_humidity_2m")
                .queryParam("timezone", "America/Bogota")
                .build())
        .retrieve()
        .body(Map.class);

Map<String, Object> current = (Map<String, Object>) data.get("current");
return "Temperatura ambiental: " + current.get("temperature_2m")
        + " °C | Humedad: " + current.get("relative_humidity_2m") + " %";
```

El JSON devuelto por Open-Meteo tiene la estructura:
```json
{
  "latitude": 7.875,
  "longitude": -76.75,
  "timezone": "America/Bogota",
  "current": {
    "time": "2026-11-11T14:00",
    "temperature_2m": 29.4,
    "relative_humidity_2m": 78
  }
}
```
El servicio extrae el objeto `current`, formatea los valores de temperatura (`°C`) y humedad (`%`), incluye manejo de excepciones (`try/catch`) para garantizar resiliencia si no hay conexión a Internet, y entrega el resultado al `WebController` para renderizarlo en el Panel de Control (`inicio.html`).

---

## 4. Guion sugerido para el video de sustentación (2 a 3 minutos)

1. **Minuto 0:00 – 0:30 (Presentación y Arquitectura SOLID):**
   * Mostrar la estructura de paquetes en VS Code (`controller`, `service`, `repository`/`dao`, `model`).
   * Explicar brevemente **SRP** (`WebController` para vistas, `InventarioService` para reglas de negocio, `ClimaService` para la API y `Repository`/`GenericDAO` para MySQL) y **DIP** (inyección de dependencias por constructor en `InventarioService`).
2. **Minuto 0:30 – 1:10 (Panel Principal y Consumo de API Externa Open-Meteo):**
   * Iniciar sesión en `http://localhost:8080` con `admin` / `admin`.
   * Mostrar en el Panel de Control la sección **API externa Open-Meteo** con la temperatura ambiental y humedad de Urabá, y mostrar en `ClimaService.java` la petición HTTP GET que procesa el JSON.
3. **Minuto 1:10 – 2:30 (Demostración del Flujo Completo y Base de Datos MySQL):**
   * Ir a **Productos** y registrar *Bocachico* (`Prochilodus magdalenae`, `$15000`).
   * Ir a **Lotes** y crear el lote `LOT-001` con `100 kg`, ubicación `Nevera 1` y rango de `0 °C` a `4 °C`.
   * Ir a **Cadena de frío**, registrar una lectura normal (`2.0 °C`) y luego una lectura fuera de rango (`8.0 °C`) para mostrar cómo se activa la **ALERTA: FUERA DE RANGO**.
   * Ir a **Ventas y mermas**, registrar una venta de `20 kg` (verificando que el stock baje a `80 kg` y calcule `$300.000 COP`), e intentar vender `90 kg` para demostrar la validación de *"Inventario insuficiente"*.
   * Ir a **Reportes** y mostrar el consolidado junto con el botón **Imprimir / Guardar PDF** y las tablas en MySQL Workbench.
