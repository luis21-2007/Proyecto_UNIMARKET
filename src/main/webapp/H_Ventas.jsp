<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Venta</title>

    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">
    <link rel="stylesheet" href="assets/css/estilos-historiales-compraventa.css">
</head>
<body>

<%@ include file="layout/header.jsp" %>

<div class="container mt-5">
    <div class="title-divider">
        <h1 class="page-title">Historial de Venta</h1>
        <div class="page-subtitle">Consulta los Productos que has vendido.</div>
    </div>

    <div class="row">
        <div class="col-12 col-lg-10">

            <!-- Ítem 1 -->
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

<script src="assets/js/bootstrap.js"></script>
</body>
</html>
