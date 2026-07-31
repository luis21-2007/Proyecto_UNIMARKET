<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="layout/header.jsp" %>

<div class="container-fluid py-3 main-market-container">
    <!-- TEXTO DE BIENVENIDA -->
    <div class="row mb-4">
        <div class="col-12 text-center">
            <p class="welcome-banner mb-0">
                ¡Bienvenido a Marketplace Universitario de Articulos! Disfruta de una gran variedad de productos en venta por parte de los alumnos de la UTEZ
            </p>
        </div>
    </div>

    <!-- SECCIÓN DE CATEGORÍAS HORIZONTALES -->
    <div class="row align-items-center mb-5 position-relative px-md-4">
        <div class="col-11">
            <div class="category-scroll-container">
                <!-- Comida -->
                <a href="#" class="category-card text-center">
                    <img src="assets/img/icono-integradora.jpeg" alt="Comida">
                    <div class="fw-bold small">Comida</div>
                </a>
                <!-- Ropa -->
                <a href="#" class="category-card text-center">
                    <img src="assets/img/icono-integradora.jpeg" alt="Ropa">
                    <div class="fw-bold small">Ropa</div>
                </a>
                <!-- Accesorios -->
                <a href="#" class="category-card text-center">
                    <img src="assets/img/icono-integradora.jpeg" alt="Accesorios">
                    <div class="fw-bold small">Accesorios</div>
                </a>
                <!-- Electrónicos -->
                <a href="#" class="category-card text-center">
                    <img src="assets/img/icono-integradora.jpeg" alt="Electrónicos">
                    <div class="fw-bold small">Electronicos</div>
                </a>
                <!-- Cosméticos -->
                <a href="#" class="category-card text-center">
                    <img src="assets/img/icono-integradora.jpeg" alt="Cosméticos">
                    <div class="fw-bold small">Cosmeticos</div>
                </a>
                <!-- Joyería -->
                <a href="#" class="category-card text-center">
                    <img src="assets/img/icono-integradora.jpeg" alt="Joyería">
                    <div class="fw-bold small">Joyeria</div>
                </a>
            </div>
        </div>
        <!-- Flecha de navegación derecha -->
        <div class="col-1 text-end d-none d-md-block">
            <i class="bi bi-chevron-right fs-2 fw-bold text-dark cursor-pointer" style="cursor: pointer;"></i>
        </div>
    </div>

    <!-- CUADRÍCULA DE PRODUCTOS -->
    <div class="px-md-4">
        <div class="row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-5 row-cols-xl-6 g-4">


            <!-- Producto 2: Tortas -->
            <div class="col">
                <div class="product-card">
                    <div class="card-img-wrapper">
                        <img src="assets/img/icono-integradora.jpeg" alt="Tortas de milanesa">
                    </div>
                    <div class="product-title text-truncate">Tortas de milanesa</div>
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <span class="product-price">$35 c/u</span>
                        <span class="product-rating"><i class="bi bi-star-fill me-1"></i>4.5</span>
                    </div>
                    <button class="btn btn-comprar shadow-sm">Revisar</button>
                </div>
            </div>


            <!-- Producto 4: Pays -->
            <div class="col">
                <div class="product-card">
                    <div class="card-img-wrapper">
                        <img src="assets/img/icono-integradora.jpeg" alt="Pays de varios sabores">
                    </div>
                    <div class="product-title text-truncate">Pays de varios sabores</div>
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <span class="product-price">$30 c/u</span>
                        <span class="product-rating"><i class="bi bi-star-fill me-1"></i>4.5</span>
                    </div>
                    <button class="btn btn-comprar shadow-sm">Revisar</button>
                </div>
            </div>

            <!-- Producto 5: Llaveros Duo -->
            <div class="col">
                <div class="product-card">
                    <div class="card-img-wrapper">
                        <img src="assets/img/icono-integradora.jpeg" alt="Llaveros Duo">
                    </div>
                    <div class="product-title text-truncate">Llaveros Duo</div>
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <span class="product-price">$25 c/u</span>
                        <span class="product-rating"><i class="bi bi-star-fill me-1"></i>4.5</span>
                    </div>
                    <button class="btn btn-comprar shadow-sm">Revisar</button>
                </div>
            </div>



<%@ include file="layout/footer.jsp" %>