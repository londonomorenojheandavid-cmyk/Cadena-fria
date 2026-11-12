# Changelog
Formato: [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) · Versionamiento: [SemVer](https://semver.org/lang/es/)

## [1.0.0] - 2026-11-11 · Implementación completa en Java (Fases I, II y III)
### Agregado
- **Fase I:** Esquema MySQL 8 (`sql/crear_bd.sql` y `data/schema.sql`), configuración Hibernate 6 (`hibernate.cfg.xml`, `HibernateUtil`), `ClimaApiClient` (Open-Meteo), `PruebaConexion`, Servlets y vistas JSP.
- **Fase II:** Mapeo ORM de las 7 entidades (`Cooperativa`, `Usuario`, `Cava`, `Producto`, `LoteInventario`, `LecturaTemperatura`, `Alerta`), patrón `GenericDAO<T, ID>` / `GenericDAOImpl<T, ID>`, DAOs por entidad y clase `com.cadenafrio.Main` con operaciones CRUD.
- **Fase III:** Aplicación web completa **FríoPesca** con Spring Boot 3, Spring Security, Thymeleaf y MySQL (`FrioPescaApplication`, `WebController`, `InventarioService`, `ClimaService`, gestión de productos, lotes, temperaturas, ventas/mermas y reportes en PDF).
### Eliminado
- Código previo en Python reemplazado íntegramente por la arquitectura en Java con Maven, Hibernate y Spring Boot.
