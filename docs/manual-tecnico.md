# Manual Técnico — CadenaFría Urabá / FríoPesca

## 1. Stack Tecnológico
* **Lenguaje:** Java 17 / 21
* **Gestor de construcción:** Apache Maven (`pom.xml`)
* **ORM y Persistencia:** Hibernate ORM 6 + Spring Data JPA + Conector JDBC `mysql-connector-j`
* **Base de Datos:** MySQL 8 (`sql/crear_bd.sql` y `data/schema.sql`)
* **Framework Web y Seguridad:** Spring Boot 3.3.5, Spring Security, Thymeleaf, Jakarta Servlets/JSP
* **API Externa:** Open-Meteo REST API (`https://api.open-meteo.com/v1/forecast`)

## 2. Organización de Paquetes
* `com.cadenafrio.model`: Entidades JPA/Hibernate de las Fases I y II (`Cooperativa`, `Usuario`, `Cava`, `Producto`, `LoteInventario`, `LecturaTemperatura`, `Alerta`) y enumeraciones (`Rol`, `EstadoLote`).
* `com.cadenafrio.dao`: Interfaz genérica `GenericDAO<T, ID>`, implementación `GenericDAOImpl<T, ID>` con manejo de transacciones (`persist`, `find`, `merge`, `remove`) y DAOs específicos por entidad.
* `com.cadenafrio.util`: `HibernateUtil` (`SessionFactory` a partir de `hibernate.cfg.xml`) y `ClimaApiClient`.
* `com.cadenafrio`: Clases ejecutables `PruebaConexion` (Fase I) y `Main` (demostración CRUD de Fase II).
* `com.friopesca`: Aplicación web completa de Fase III (`FrioPescaApplication`, `SecurityConfig`, `WebController`, `InventarioService`, `ClimaService`, entidades y repositorios JPA).

## 3. Ejecución de Pruebas
```bash
mvn test
```
