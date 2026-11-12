# FríoPesca / CadenaFría Urabá — Sistema de inventario y cadena de frío para comerciantes de pescado

* **Estudiante:** Jean David Londoño Moreno
* **Asignatura:** Lenguaje de Programación III
* **Docente:** Marta Lucía Jiménez Torres
* **Institución:** Corporación Universitaria Remington — Uniremington (Facultad de Ingenierías)
* **Repositorio:** `https://github.com/londonomorenojheandavidcmyk/cadenafria_uraba.git`

---

## 1. Descripción del proyecto

El sistema permite a las cooperativas pesqueras y comerciantes de pescado de **Necoclí y Turbo (Urabá)** administrar productos pesqueros, lotes de inventario, cantidades disponibles, movimientos (entradas, ventas y mermas), lecturas de temperatura en cavas/refrigeradores, alertas de cadena de frío y reportes operativos. Además, consume la API REST externa **Open-Meteo** para consultar la temperatura y humedad ambiente exterior de la zona.

El proyecto integra las tres fases del curso en **Java**:

1. **Fase I (`com.cadenafrio`):** Planificación, modelo entidad-relación en MySQL 8 (`cadena_frio`), configuración de Hibernate 6 (`hibernate.cfg.xml`, `HibernateUtil`), cliente de clima (`ClimaApiClient`), servlets (`DashboardServlet`, `LoteServlet`), vistas JSP (`dashboard.jsp`, `lote_form.jsp`, `reporte.jsp`) y verificación de conexión (`PruebaConexion`).
2. **Fase II (`com.cadenafrio.model` y `com.cadenafrio.dao`):** Mapeo ORM con anotaciones JPA/Hibernate de las 7 entidades (`Cooperativa`, `Usuario`, `Cava`, `Producto`, `LoteInventario`, `LecturaTemperatura`, `Alerta`), patrón DAO genérico (`GenericDAO<T, ID>`, `GenericDAOImpl<T, ID>` y DAOs por entidad) y demostración de operaciones CRUD (`com.cadenafrio.Main`).
3. **Fase III (`com.friopesca`):** Aplicación web completa con **Spring Boot 3, Spring Data JPA/Hibernate, Spring Security, Thymeleaf y MySQL 8**, incluyendo autenticación administrativa, panel de control con Open-Meteo, catálogo de productos con desactivación lógica, inventario por lotes, control manual de temperatura con alertas fuera de rango, ventas y mermas con validación de existencias y reportes imprimibles en PDF.

---

## 2. Estructura del proyecto

```text
cadenafria-uraba/
├── pom.xml
├── sql/
│   └── crear_bd.sql
├── data/
│   └── schema.sql
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── com/cadenafrio/                 # Fases I y II (Hibernate ORM + DAO + CRUD)
    │   │   │   ├── model/                      # Cooperativa, Usuario, Cava, Producto, LoteInventario, LecturaTemperatura, Alerta, Rol, EstadoLote
    │   │   │   ├── dao/                        # GenericDAO, GenericDAOImpl, CooperativaDAO, UsuarioDAO, CavaDAO, ProductoDAO, LoteInventarioDAO, LecturaTemperaturaDAO, AlertaDAO
    │   │   │   ├── service/                    # InventarioService, AlertaService
    │   │   │   ├── controller/                 # DashboardServlet, LoteServlet
    │   │   │   ├── util/                       # HibernateUtil, ClimaApiClient
    │   │   │   ├── PruebaConexion.java         # Fase I: Verificación de conexión JDBC/Hibernate
    │   │   │   └── Main.java                   # Fase II: Prueba de operaciones CRUD (Cava, Producto, LoteInventario)
    │   │   └── com/friopesca/                  # Fase III (Aplicación Web Spring Boot)
    │   │       ├── config/SecurityConfig.java
    │   │       ├── controller/WebController.java
    │   │       ├── model/                      # Producto, Lote, Movimiento, LecturaTemperatura
    │   │       ├── repository/                 # ProductoRepository, LoteRepository, MovimientoRepository, LecturaTemperaturaRepository
    │   │       ├── service/                    # InventarioService, ClimaService
    │   │       └── FrioPescaApplication.java
    │   ├── resources/
    │   │   ├── hibernate.cfg.xml               # Configuración Hibernate 6 (Fases I y II)
    │   │   ├── application.properties          # Configuración Spring Boot (Fase III)
    │   │   ├── static/
    │   │   │   ├── css/app.css
    │   │   │   └── prototipo.html              # Prototipo interactivo HTML
    │   │   └── templates/                      # Vistas Thymeleaf (login, inicio, productos, lotes, movimientos, temperaturas, reportes)
    │   └── webapp/
    │       ├── WEB-INF/web.xml
    │       └── vistas/                         # dashboard.jsp, lote_form.jsp, reporte.jsp
    └── test/
        └── java/com/friopesca/
            └── InventarioServiceTest.java
```

---

## 3. Requisitos y puesta en marcha

### Requisitos
- **Java JDK 17 o 21**
- **Apache Maven 3.9+**
- **MySQL Server 8** y MySQL Workbench
- **VS Code** con Extension Pack for Java (o IntelliJ IDEA)

### Paso 1: Crear la base de datos en MySQL
Ejecuta en MySQL Workbench el archivo [`sql/crear_bd.sql`](sql/crear_bd.sql), el cual crea las bases de datos `friopesca` y `cadena_frio` junto con todas las tablas y relaciones.

### Paso 2: Ejecutar la aplicación web FríoPesca (Fase III)
Abre PowerShell en la carpeta del proyecto y ejecuta:

```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="TU_CLAVE_MYSQL"
$env:ADMIN_USER="admin"
$env:ADMIN_PASSWORD="CAMBIA_ESTA_CLAVE_SEGURA"
mvn spring-boot:run
```

Cuando Spring Boot inicie, abre **`http://localhost:8080`** e inicia sesión con las credenciales configuradas (por defecto `admin` / `admin` si no defines variables de entorno).

### Paso 3: Ejecutar la demostración CRUD de Hibernate (Fase II)
Para ejecutar la clase `com.cadenafrio.Main` que demuestra las operaciones `CREATE`, `READ`, `UPDATE` y `DELETE` sobre `Cava`, `Producto` y `LoteInventario`:

```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="TU_CLAVE_MYSQL"
mvn compile exec:java "-Dexec.mainClass=com.cadenafrio.Main"
```

---

## 4. Principios SOLID aplicados
- **SRP (Responsabilidad Única):** Separación estricta entre entidades de dominio (`model`), acceso a datos (`dao` / `repository`), lógica de negocio (`InventarioService`, `AlertaService`, `ClimaService`) y capa de presentación (`controller`).
- **DIP (Inversión de Dependencias):** Los servicios reciben sus repositorios/DAOs por constructor y operan sobre abstracciones (`GenericDAO<T, ID>`, `JpaRepository`).
