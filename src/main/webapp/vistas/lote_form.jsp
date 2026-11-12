<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>CadenaFría Urabá - Ingreso de Lote</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
    <h2>Formulario de Ingreso de Lote</h2>
    <form method="post" action="../lotes" class="card p-4 shadow-sm mt-3">
        <div class="mb-3">
            <label class="form-label">Producto (especie)</label>
            <select name="productoId" class="form-select" required>
                <option value="1">Pargo Rojo</option>
                <option value="2">Bocachico</option>
                <option value="3">Tilapia</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Cava de destino</label>
            <select name="ubicacion" class="form-select" required>
                <option value="Cava 1">Cava 1</option>
                <option value="Cava 2">Cava 2</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Cantidad (kg)</label>
            <input type="number" step="0.01" min="0.01" name="kilos" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Fecha de vencimiento</label>
            <input type="date" name="vencimiento" class="form-control" required>
        </div>
        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">Guardar</button>
            <a href="dashboard.jsp" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</div>
</body>
</html>
