<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Compras</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

    <style>
        body {
            background-color: #faf7f5;
        }

        /* Estilos del encabezado del historial */
        .page-title {
            font-size: 2.8rem;
            font-weight: 900;
            margin-bottom: 0px;
        }

        .page-subtitle {
            color: #7a7a7a;
            font-weight: bold;
            font-size: 1.1rem;
        }

        .title-divider {
            border-bottom: 3px solid #f38a55; /* Línea naranja */
            display: inline-block;
            padding-bottom: 5px;
            margin-bottom: 25px;
            width: fit-content;
        }

        /* Estilos de la tarjeta de compra */
        .purchase-card {
            background-color: #f2f3f4;
            border: 1px solid #6c757d;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }

        .product-img {
            width: 170px;
            height: 110px;
            object-fit: cover;
            border-radius: 8px;
        }

        .product-title {
            font-weight: 900;
            font-size: 1.8rem;
            margin-bottom: 0;
        }

        .vendor-text, .date-text {
            color: #8a8a8a;
            font-weight: 700;
            font-size: 1.1rem;
            margin-bottom: 5px;
        }

        .price-text {
            color: #d67a42;
            font-size: 1.8rem;
            font-weight: bold;
        }

        /* Estilos de los botones tipo "Pill" */
        .btn-pill {
            border-radius: 20px;
            font-weight: bold;
            padding: 2px 18px;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
            font-size: 1rem;
            text-align: center;
        }

        .btn-entregado {
            border: 1.5px solid #28a745;
            color: #28a745;
            background-color: transparent;
        }

        .btn-calificar, .btn-reportar {
            border: 1.5px solid #8b0000;
            color: #8b0000;
            background-color: transparent;
        }

        /* Efecto: Se ponen rojos y con texto blanco */
        .btn-calificar:hover,
        .btn-reportar:hover {
            background-color: #dc3545 !important;
            color: #ffffff !important;
            border-color: #dc3545 !important;
        }
    </style>
</head>
<body>

<%@ include file="layout/header.jsp" %>

<div class="container mt-5">
    <!-- Título y Subtítulo -->
    <div class="title-divider">
        <h1 class="page-title">Historial de compras</h1>
        <div class="page-subtitle">Consulta compras realizadas.</div>
    </div>

    <!-- LISTA DE COMPRAS -->
    <div class="row">
        <div class="col-12 col-lg-10"> <!-- Limitamos el ancho para que parezca la imagen -->

            <!-- Ítem 1 -->
            <div class="purchase-card d-flex flex-column flex-md-row align-items-center position-relative">
                <img src="ruta/a/tu/imagen_torta.jpg" alt="Torta de Milanesa" class="product-img me-4 mb-3 mb-md-0">

                <!-- Detalles del producto -->
                <div class="flex-grow-1">
                    <h2 class="product-title">Torta de Milanesa</h2>
                    <p class="vendor-text">Vendedor:Juan_Torte</p>
                    <p class="date-text mb-0">20/06/2026</p>
                </div>

                <!-- Botones y Precio -->
                <div class="d-flex flex-column align-items-end justify-content-between" style="min-height: 110px;">
                    <div class="d-flex gap-2">
                        <a class="btn-pill btn-entregado">Entregado</a>
                        <a href="calificarCompra.jsp" class="btn-pill btn-calificar">Calificar</a>
                    </div>
                    <div class="d-flex gap-2 mt-2 align-items-center">
                        <a href="reportarCompra.jsp" class="btn-pill btn-reportar me-4">Reportar</a>
                        <span class="price-text">$67</span>
                    </div>
                </div>
            </div>

            <!-- Ítem 2 -->
            <div class="purchase-card d-flex flex-column flex-md-row align-items-center position-relative">
                <img src="ruta/a/tu/imagen_torta.jpg" alt="Torta de Milanesa" class="product-img me-4 mb-3 mb-md-0">
                <div class="flex-grow-1">
                    <h2 class="product-title">Torta de Milanesa</h2>
                    <p class="vendor-text">Vendedor:Juan_Torte</p>
                    <p class="date-text mb-0">20/06/2026</p>
                </div>
                <div class="d-flex flex-column align-items-end justify-content-between" style="min-height: 110px;">
                    <div class="d-flex gap-2">
                        <a class="btn-pill btn-entregado">Entregado</a>
                        <a href="calificarCompra.jsp" class="btn-pill btn-calificar">Calificar</a>
                    </div>
                    <div class="d-flex gap-2 mt-2 align-items-center">
                        <a href="reportarCompra.jsp" class="btn-pill btn-reportar me-4">Reportar</a>
                        <span class="price-text">$67</span>
                    </div>
                </div>
            </div>

            <!-- Ítem 3 -->
            <div class="purchase-card d-flex flex-column flex-md-row align-items-center position-relative">
                <img src="ruta/a/tu/imagen_torta.jpg" alt="Torta de Milanesa" class="product-img me-4 mb-3 mb-md-0">
                <div class="flex-grow-1">
                    <h2 class="product-title">Torta de Milanesa</h2>
                    <p class="vendor-text">Vendedor:Juan_Torte</p>
                    <p class="date-text mb-0">20/06/2026</p>
                </div>
                <div class="d-flex flex-column align-items-end justify-content-between" style="min-height: 110px;">
                    <div class="d-flex gap-2">
                        <a class="btn-pill btn-entregado">Entregado</a>
                        <a href="calificarCompra.jsp" class="btn-pill btn-calificar">Calificar</a>
                    </div>
                    <div class="d-flex gap-2 mt-2 align-items-center">
                        <a href="reportarCompra.jsp" class="btn-pill btn-reportar me-4">Reportar</a>
                        <span class="price-text">$67</span>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>