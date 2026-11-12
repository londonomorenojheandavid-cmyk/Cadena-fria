<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>CadenaFría Urabá - Reporte de Inventario y Alertas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Reporte de Inventario y Bitácora de Alertas</h2>
        <button class="btn btn-primary" onclick="window.print()">Exportar / Imprimir PDF</button>
    </div>
    <div class="card p-3 shadow-sm mb-4">
        <h5>Filtros de Inventario</h5>
        <div class="row g-2">
            <div class="col-md-4"><input class="form-control" placeholder="Filtrar por cava..."></div>
            <div class="col-md-4"><input class="form-control" placeholder="Filtrar por especie..."></div>
            <div class="col-md-4">
                <select class="form-select">
                    <option value="">Todos los estados</option>
                    <option value="DISPONIBLE">DISPONIBLE</option>
                    <option value="AGOTADO">AGOTADO</option>
                    <option value="DESCARTADO">DESCARTADO</option>
                </select>
            </div>
        </div>
    </div>
</div>
</body>
</html>
