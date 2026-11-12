# Manual de Usuario e Instalación — FríoPesca

* **Sistema de inventario y cadena de frío para comerciantes de pescado**
* **Responsable:** Londoño Moreno Jean David
* **Asignatura:** Lenguaje de Programación III
* **Docente:** Marta Lucía Jiménez Torres

## 1. Presentación y alcance
FríoPesca es una aplicación web académica en Java que permite administrar productos pesqueros, lotes, cantidades disponibles, movimientos de inventario, lecturas manuales de temperatura y reportes. También presenta información meteorológica exterior desde Open-Meteo.

## 2. Requisitos y puesta en marcha
1. Instala Java JDK 17/21, Maven 3.9 o superior y MySQL 8.
2. En MySQL Workbench ejecuta el archivo `sql/crear_bd.sql` para crear la base de datos.
3. Abre PowerShell en la carpeta del proyecto y ejecuta:
```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="TU_CLAVE_MYSQL"
$env:ADMIN_USER="admin"
$env:ADMIN_PASSWORD="CAMBIA_ESTA_CLAVE_SEGURA"
mvn spring-boot:run
```
4. Visita `http://localhost:8080` y accede con las credenciales configuradas.

## 3. Flujo operativo en el sistema
1. **Ingreso al sistema (`/login`):** Introduce usuario y contraseña de administrador.
2. **Panel principal (`/`):** Revisa el resumen de productos, lotes, alertas históricas, movimientos y la consulta meteorológica exterior desde Open-Meteo.
3. **Gestión de productos (`/productos`):** Registra nombre, especie y precio por kilogramo (COP). Utiliza la opción *Desactivar* cuando un producto deje de comercializarse conservando su historial.
4. **Registro de lotes (`/lotes`):** Selecciona un producto activo, código único (ej. `LOT-001`), kilogramos iniciales, fechas de ingreso y vencimiento, ubicación (`Nevera 1` / `Cava 1`) y rango de temperatura mínima y máxima (°C).
5. **Control de cadena de frío (`/temperaturas`):** Registra lecturas manuales de temperatura indicando lote, °C, responsable y observación. Si la temperatura está fuera del rango configurado, el sistema marca `FUERA DE RANGO` y emite la alerta visual.
6. **Ventas y mermas (`/movimientos`):** Registra salidas por `VENTA` o `MERMA`. El sistema calcula el valor en COP para ventas y rechaza salidas superiores al stock disponible (`Inventario insuficiente`).
7. **Reportes operativos (`/reportes`):** Consulta existencias por lote, movimientos y temperaturas, y utiliza el botón **Imprimir / Guardar PDF**.
