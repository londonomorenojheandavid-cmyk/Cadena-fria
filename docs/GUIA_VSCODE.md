# Guía rápida en Visual Studio Code

1. **Abrir el proyecto:** `File > Open Folder…` → `cadenafria-uraba`. Instala las extensiones recomendadas (*Extension Pack for Java* y *Spring Boot Extension Pack*).
2. **Base de datos:** Ejecuta `sql/crear_bd.sql` en MySQL Workbench.
3. **Ejecutar la aplicación web (Fase III):** `Ctrl+Shift+P` → *Tasks: Run Task* → `Spring Boot: Ejecutar FríoPesca (mvn spring-boot:run)` y abre `http://localhost:8080`.
4. **Ejecutar demostración CRUD Hibernate (Fase II):** `Ctrl+Shift+P` → *Tasks: Run Task* → `Hibernate Fase II: Ejecutar Main CRUD`.
5. **Ejecutar pruebas unitarias:** `Ctrl+Shift+P` → *Tasks: Run Test Task* (`mvn test`).
