<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Venta</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

    <style>
        body {
            background-color: #faf7f5;
        }

        /* Estilos del encabezado */
        .page-title {
            font-size: 2.8rem;
            font-weight: 900;
            margin-bottom: 0px;
            color: #000;
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

        /* Estilos de la tarjeta de venta */
        .sale-card {
            background-color: #f2f3f4;
            border: 1px solid #6c757d;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }

        .product-img {
            width: 150px;
            height: 150px;
            object-fit: contain; /* Para que la ropa se vea completa sin cortarse */
            border-radius: 8px;
            background-color: transparent;
        }

        .product-title {
            font-weight: 900;
            font-size: 1.8rem;
            margin-bottom: 0;
            color: #000;
        }

        /* Textos descriptivos grises */
        .info-text {
            color: #949494;
            font-weight: 700;
            font-size: 1.1rem;
            margin-bottom: 2px;
        }

        .price-text {
            color: #e67e22; /* Naranja para el precio */
            font-size: 2rem;
            font-weight: bold;
        }

        /* Badge/Pill de "Completada" */
        .badge-completada {
            border: 1.5px solid #28a745;
            color: #28a745;
            background-color: transparent;
            border-radius: 20px;
            font-weight: bold;
            padding: 4px 20px;
            display: inline-block;
            font-size: 1.1rem;
        }
    </style>
</head>
<body>

<%@ include file="layout/header.jsp" %>

<div class="container mt-5">
    <div class="title-divider">
        <h1 class="page-title">Historial de Venta</h1>
        <div class="page-subtitle">Consulta los Productos que has vendido.</div>
    </div>

    <!-- LISTA DE VENTAS -->
    <div class="row">
        <div class="col-12 col-lg-10"> <!-- Límite de ancho -->

            <!-- Ítem 1 -->
            <div class="sale-card d-flex flex-column flex-md-row align-items-center position-relative">
                <img src="ruta/a/tu/imagen_hoodie.png" alt="Hoodie Essentials" class="product-img me-4 mb-3 mb-md-0">

                <!-- Detalles del producto -->
                <div class="flex-grow-1">
                    <h2 class="product-title">Hoodie Essentials</h2>
                    <p class="info-text">ID:V0005</p>
                    <p class="info-text mt-2">Vendido a: Irving_Flores</p>
                    <p class="info-text mt-2 mb-0">20/06/2026</p>
                </div>

                <!-- Estado y Precio -->
                <div class="d-flex flex-column align-items-end justify-content-between h-100 py-2" style="min-height: 140px;">
                    <div>
                        <span class="badge-completada">Completada</span>
                    </div>
                    <div class="mt-auto">
                        <span class="price-text">$400</span>
                    </div>
                </div>
            </div>

            <!-- Ítem 2 -->
            <div class="sale-card d-flex flex-column flex-md-row align-items-center position-relative">
                <img src="ruta/a/tu/imagen_hoodie.png" alt="Hoodie Essentials" class="product-img me-4 mb-3 mb-md-0">
                <div class="flex-grow-1">
                    <h2 class="product-title">Hoodie Essentials</h2>
                    <p class="info-text">ID:V0005</p>
                    <p class="info-text mt-2">Vendido a: Irving_Flores</p>
                    <p class="info-text mt-2 mb-0">20/06/2026</p>
                </div>
                <div class="d-flex flex-column align-items-end justify-content-between h-100 py-2" style="min-height: 140px;">
                    <div>
                        <span class="badge-completada">Completada</span>
                    </div>
                    <div class="mt-auto">
                        <span class="price-text">$400</span>
                    </div>
                </div>
            </div>

            <!-- Ítem 3 -->
            <div class="sale-card d-flex flex-column flex-md-row align-items-center position-relative">
                <img src="ruta/a/tu/imagen_hoodie.png" alt="Hoodie Essentials" class="product-img me-4 mb-3 mb-md-0">
                <div class="flex-grow-1">
                    <h2 class="product-title">Hoodie Essentials</h2>
                    <p class="info-text">ID:V0005</p>
                    <p class="info-text mt-2">Vendido a: Irving_Flores</p>
                    <p class="info-text mt-2 mb-0">20/06/2026</p>
                </div>
                <div class="d-flex flex-column align-items-end justify-content-between h-100 py-2" style="min-height: 140px;">
                    <div>
                        <span class="badge-completada">Completada</span>
                    </div>
                    <div class="mt-auto">
                        <span class="price-text">$400</span>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>