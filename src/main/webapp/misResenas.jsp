<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Reseñas</title>

    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">
    <link rel="stylesheet" href="assets/css/estilos-historiales-compraventa.css">
</head>
<body>

<%@ include file="layout/header.jsp" %>

<div class="container mt-5">

    <div class="title-divider">
        <h1 class="page-title">Mis Reseñas</h1>
        <div class="page-subtitle">
            Consulta las reseñas que otros usuarios han dejado sobre tus ventas.
        </div>
    </div>

    <div class="row">
        <div class="col-12 col-lg-10">

            <!-- Reseña 1 -->
            <div class="sale-card d-flex flex-column flex-md-row align-items-center">

                <img src="assets/img/productos/hoodie.png"
                     class="product-img me-4 mb-3 mb-md-0"
                     alt="Producto">

                <div class="flex-grow-1">

                    <h2 class="product-title">
                        Hoodie Essentials
                    </h2>

                    <p class="info-text">
                        Comprador: Irving_Flores
                    </p>

                    <p class="info-text">
                        Fecha: 20/06/2026
                    </p>

                    <div class="mt-2">

                        <span class="text-warning">
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-fill"></i>
                        </span>

                    </div>

                    <p class="mt-3 mb-0">
                        Excelente vendedor. El producto llegó exactamente como se mostraba en la publicación.
                    </p>

                </div>

            </div>

            <!-- Reseña 2 -->
            <div class="sale-card d-flex flex-column flex-md-row align-items-center">

                <img src="assets/img/productos/audifonos.png"
                     class="product-img me-4 mb-3 mb-md-0"
                     alt="Producto">

                <div class="flex-grow-1">

                    <h2 class="product-title">
                        Audífonos Sony
                    </h2>

                    <p class="info-text">
                        Comprador: Daniela_Mtz
                    </p>

                    <p class="info-text">
                        Fecha: 15/06/2026
                    </p>

                    <div class="mt-2">

                        <span class="text-warning">
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star"></i>
                        </span>

                    </div>

                    <p class="mt-3 mb-0">
                        Muy buena atención. Contestó rápido y entregó el producto en tiempo.
                    </p>

                </div>

            </div>

            <!-- Reseña 3 -->
            <div class="sale-card d-flex flex-column flex-md-row align-items-center">

                <img src="assets/img/productos/mouse.png"
                     class="product-img me-4 mb-3 mb-md-0"
                     alt="Producto">

                <div class="flex-grow-1">

                    <h2 class="product-title">
                        Mouse Logitech
                    </h2>

                    <p class="info-text">
                        Comprador: Luis_Ramirez
                    </p>

                    <p class="info-text">
                        Fecha: 11/06/2026
                    </p>

                    <div class="mt-2">

                        <span class="text-warning">
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-fill"></i>
                            <i class="bi bi-star-half"></i>
                        </span>

                    </div>

                    <p class="mt-3 mb-0">
                        Producto en excelente estado. Recomiendo totalmente al vendedor.
                    </p>

                </div>

            </div>

        </div>
    </div>

</div>

<script src="assets/js/bootstrap.js"></script>

</body>
</html>