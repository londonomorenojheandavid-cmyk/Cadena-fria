# Guía rápida en Visual Studio Code

1. **Abrir:** `File > Open Workspace from File…` → `cadenafria-uraba.code-workspace`. Acepta instalar las extensiones recomendadas (GitLens, Git Graph, GitHub Pull Requests, GitHub Actions).
2. **Pruebas:** `Ctrl+Shift+P` → *Tasks: Run Test Task* (14 pruebas en la rama de la OCI; 8 en `v1.0.0`).
3. **Ver ramas:** botón *Git Graph* en la barra inferior (muestra `main`, `develop`, `feature/OCI-001-…` y los tags).
4. **Publicar:** terminal (`Ctrl+ñ`) → `gh auth login` → `git switch feature/OCI-001-reporte-trazabilidad` → `bash scripts/publicar_en_github.sh`.
   En Windows usa *Git Bash* (terminal de VS Code → perfil Git Bash).
5. **Pull Request:** pestaña *GitHub Pull Requests* → abrir el PR de la OCI, mostrar la plantilla y aprobar/fusionar.
6. **Auditoría en el video:** `Ctrl+Shift+P` → *Tasks: Run Task* → «Auditoria: diff contra linea base v1.0.0» y «Auditoria: autor y fecha por commit».
