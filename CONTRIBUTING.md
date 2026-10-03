# Guía de contribución y control de configuración

Este documento es el **procedimiento oficial de control de versiones** del proyecto (Unidad 2, Tema 1:
control de versiones, control de cambios y auditoría de configuración).

## 1. Estrategia de ramas (GitFlow simplificado)

| Rama | Propósito | Se crea desde | Se integra en | Protección |
|---|---|---|---|---|
| `main` | Solo versiones liberadas y etiquetadas (`vX.Y.Z`). Siempre desplegable. | — | — | PR obligatorio, 1 aprobación, CI verde, sin push directo ni force-push |
| `develop` | Integración continua del siguiente release. | `main` | `main` (release) | PR obligatorio, CI verde |
| `feature/OCI-NNN-nombre` | Una rama por cada OCI/historia. | `develop` | `develop` (vía PR) | Se elimina al fusionar |
| `release/X.Y.Z` | Estabilización y ajuste de VERSION/CHANGELOG (opcional). | `develop` | `main` y `develop` | PR |
| `hotfix/NNN-nombre` | Corrección urgente sobre producción. | `main` | `main` y `develop` | PR + tag PATCH |

```
main     ●────────────●(v1.0.0)──────────────────────●(v1.1.0)
          \          /                              /
develop    ●──●──●──●──────────●──────────●─────────●
                              \         /
feature/OCI-001-...            ●──●──●──●  (PR abierto → ACC)
```

## 2. Flujo de control de cambios (ACC / OCI)

1. Se abre un **Issue** con la plantilla *Petición de Cambio (OCI)*.
2. Se hace el **análisis de impacto** (esfuerzo, efectos secundarios, riesgos, costos).
3. El **Comité de Control de Configuración (ACC)** decide: aprobada / rechazada / incompleta.
4. Si es aprobada se emite la **Orden de Cambio de Ingeniería (OCI)** en `docs/oci/` y se asigna al ingeniero.
5. Se crea `feature/OCI-NNN-nombre` desde `develop`, se implementa, se prueba y se abre un **Pull Request**.
6. **Revisión Técnica Formal (RTF)** + auditoría con la lista de `docs/auditoria/checklist-auditoria.md`.
7. Se fusiona a `develop` (merge `--no-ff` para conservar la trazabilidad) y se libera a `main` con tag.
8. Se emite el **Informe de Estado / Dictamen** (`docs/auditoria/`).

## 3. Convención de commits (Conventional Commits + referencia al ECS)

```
<tipo>(<ámbito>): <descripción en imperativo>  [OCI-NNN|HU-NN]
```

Tipos: `feat`, `fix`, `docs`, `test`, `ci`, `chore`, `refactor`, `release`.
Ejemplo: `feat(reportes): exportar reporte de trazabilidad por lote en CSV [OCI-001]`.

## 4. Política de versionamiento (SemVer 2.0.0)

`MAJOR.MINOR.PATCH`

* **MAJOR**: cambios incompatibles (contrato de eventos, esquema de datos sin migración).
* **MINOR**: funcionalidad nueva compatible hacia atrás (ej. **v1.0.0 → v1.1.0** por OCI-001).
* **PATCH**: correcciones compatibles (hotfix).
* Pre-lanzamientos: `v1.1.0-rc.1` mientras el PR está pendiente de aprobación del ACC.
* Cada tag es **anotado** (`git tag -a`) y queda asociado a una **línea base** (LB-1 = v1.0.0, LB-2 = v1.1.0).
* Las copias de seguridad son el propio repositorio remoto + clones de los colaboradores; nadie edita copias locales fuera de Git.

## 5. Comandos de referencia

```bash
git switch develop && git pull
git switch -c feature/OCI-002-nombre
# ... trabajo y commits ...
git push -u origin feature/OCI-002-nombre   # abrir Pull Request hacia develop

# Liberación (tras aprobación del ACC)
git switch main && git merge --no-ff develop -m "release: v1.1.0"
git tag -a v1.1.0 -m "CadenaFría Urabá v1.1.0 - OCI-001"
git push origin main --tags && git switch develop && git merge main
```
