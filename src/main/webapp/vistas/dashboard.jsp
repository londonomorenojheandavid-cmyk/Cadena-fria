<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CadenaFría Urabá - Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body class="bg-light">
<nav class="navbar navbar-dark bg-primary px-4">
    <span class="navbar-brand mb-0 h1">🐟 CadenaFría Urabá · Panel de Cavas</span>
    <div>
        <a href="dashboard.jsp" class="btn btn-outline-light btn-sm me-2">Dashboard</a>
        <a href="lote_form.jsp" class="btn btn-outline-light btn-sm me-2">Ingresar Lote</a>
        <a href="reporte.jsp" class="btn btn-outline-light btn-sm">Reportes</a>
    </div>
</nav>
<div class="container py-4">
    <h2 class="mb-3">Monitoreo de Cavas y Clima Exterior (Urabá)</h2>
    <div class="alert alert-info">
        <strong>API Open-Meteo (Necoclí / Turbo):</strong> Temperatura ambiente correlacionada en tiempo real con cavas de almacenamiento (Umbral crítico: -18.0 °C).
    </div>
    <div class="card p-3 shadow-sm">
        <h5>Curva de Temperatura de Cavas (°C)</h5>
        <canvas id="tempChart" height="90"></canvas>
    </div>
</div>
<script>
    const ctx = document.getElementById('tempChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['08:00', '09:00', '10:00', '11:00', '12:00'],
                datasets: [{
                    label: 'Cava 1 (°C)',
                    data: [-21.4, -20.8, -19.5, -15.8, -21.0],
                    borderColor: '#0F4C81',
                    tension: 0.2
                }]
            }
        });
    }
</script>
</body>
</html>
