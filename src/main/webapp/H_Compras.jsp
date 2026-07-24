<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Compras</title>

    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">
    <link rel="stylesheet" href="assets/css/estilos-historiales-compraventa.css">
</head>
<body>

<%@ include file="layout/header.jsp" %>

<div class="container mt-5">
    <div class="title-divider">
        <h1 class="page-title">Historial de compras</h1>
        <div class="page-subtitle">Consulta compras realizadas.</div>
    </div>

    <div class="row">
        <div class="col-12 col-lg-10">

            <!-- Ítem 1 -->
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

<script src="assets/js/bootstrap.js"></script>
</body>
</html>
