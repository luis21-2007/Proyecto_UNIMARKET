<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Administrador - MUA</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

    <style>
        body {
            background-color: #faf7f5;
            font-family: Arial, sans-serif;
        }

        /* --- Estilos del Contenido --- */
        .page-title {
            font-weight: bold;
            font-size: 1.8rem;
            border-bottom: 3px solid #000;
            display: inline-block;
            padding-bottom: 5px;
            margin-bottom: 20px;
            margin-top: 30px;
        }

        /* Tarjetas de Estadísticas */
        .stat-card-link {
            text-decoration: none;
            color: inherit; /* Mantiene el color del texto original */
            display: block; /* Para que ocupe todo el espacio de la columna */
            height: 100%;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            border-radius: 8px;
        }

        .stat-card-link:hover {
            transform: translateY(-5px); /* Eleva la tarjeta ligeramente */
            color: inherit;
        }

        .stat-card {
            background-color: #fff;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            border: none;
            height: 100%;
            transition: box-shadow 0.2s ease;
            cursor: pointer; /* Cambia el cursor a una manita */
        }

        .stat-card-link:hover .stat-card {
            box-shadow: 0 8px 16px rgba(0,0,0,0.15); /* Aumenta la sombra al hacer hover */
        }

        .stat-title {
            font-weight: bold;
            font-size: 1.2rem;
            margin-bottom: 15px;
        }

        .stat-value {
            font-size: 3.5rem;
            font-weight: bold;
            line-height: 1;
        }

        .stat-icon {
            font-size: 4.5rem;
            color: #000;
            line-height: 1;
        }

        /* Sección de Gráfica */
        .chart-section-title {
            font-weight: bold;
            font-size: 1.5rem;
            margin-top: 40px;
            margin-bottom: 15px;
        }

        .chart-card {
            background-color: #fff;
            border-radius: 8px;
            padding: 30px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            position: relative;
        }

        .btn-detalles {
            background-color: #ffb700;
            color: #000;
            font-weight: bold;
            border-radius: 20px;
            padding: 6px 30px;
            border: none;
            position: absolute;
            top: 20px;
            right: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.2);
            transition: 0.3s;
        }

        .btn-detalles:hover {
            background-color: #e0a000;
        }
    </style>
</head>
<body>

<!-- IMPORTAR EL HEADER -->
<%@ include file="layout/header_admin.jsp" %>

<!-- CONTENIDO PRINCIPAL -->
<div class="container-fluid px-4 px-md-5 mb-5">

    <!-- Título -->
    <h1 class="page-title">Administrador</h1>

    <!-- FILA DE TARJETAS (CARDS) BOTONES -->
    <div class="row g-4">
        <!-- Card 1: Usuarios (Botón) -->
        <div class="col-12 col-md-4">
            <a href="listaUsuarios.jsp" class="stat-card-link">
                <div class="stat-card">
                    <div class="stat-title">Total de usuarios</div>
                    <div class="d-flex justify-content-between align-items-end">
                        <div class="stat-value">350</div>
                        <div class="stat-icon"><i class="bi bi-person-fill"></i></div>
                    </div>
                </div>
            </a>
        </div>

        <!-- Card 2: Productos (Botón) -->
        <div class="col-12 col-md-4">
            <a href="listaProductos.jsp" class="stat-card-link">
                <div class="stat-card">
                    <div class="stat-title">Productos subidos</div>
                    <div class="d-flex justify-content-between align-items-end">
                        <div class="stat-value">150</div>
                        <div class="stat-icon"><i class="bi bi-box-seam"></i></div>
                    </div>
                </div>
            </a>
        </div>

        <!-- Card 3: Transacciones (Botón) -->
        <div class="col-12 col-md-4">
            <a href="listaTransacciones.jsp" class="stat-card-link">
                <div class="stat-card">
                    <div class="stat-title">Total de transacciones</div>
                    <div class="d-flex justify-content-between align-items-end">
                        <div class="stat-value">180</div>
                        <div class="stat-icon"><i class="bi bi-bag"></i></div>
                    </div>
                </div>
            </a>
        </div>
    </div>
    <!-- SECCIÓN DE LA GRÁFICA -->
    <h2 class="chart-section-title">Productos Subidos por dia</h2>

    <div class="row">
        <div class="col-12">
            <div class="chart-card">
                <button class="btn btn-detalles">Detalles</button>
                <!-- Contenedor del Canvas para Chart.js -->
                <div style="height: 350px; width: 100%; margin-top: 30px;">
                    <canvas id="productosChart"></canvas>
                </div>
            </div>
        </div>
    </div>

</div>

<!-- Scripts Bootstrap y Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<!-- Script de configuración de la gráfica -->
<script>
    const ctx = document.getElementById('productosChart').getContext('2d');
    const productosChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab', 'Dom'],
            datasets: [{
                label: 'Productos Subidos',
                data: [10, 50, 75, 50, 25, 75, 100],
                borderColor: '#c64646',
                backgroundColor: 'transparent',
                pointBackgroundColor: '#fff',
                pointBorderColor: '#c64646',
                pointBorderWidth: 2,
                pointRadius: 4,
                borderWidth: 2,
                tension: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    min: 25,
                    max: 125,
                    ticks: {
                        stepSize: 25,
                        color: '#000',
                        font: { size: 14, weight: 'bold' }
                    },
                    grid: {
                        color: '#a0a0a0',
                        drawBorder: false,
                        borderDash: [10, 10]
                    }
                },
                x: {
                    ticks: {
                        color: '#000',
                        font: { size: 14, weight: 'bold' }
                    },
                    grid: {
                        display: false
                    }
                }
            }
        }
    });
</script>
<%@ include file="layout/footer.jsp" %>