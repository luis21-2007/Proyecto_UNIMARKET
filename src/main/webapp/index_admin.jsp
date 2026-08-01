<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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

<!-- Script de configuración de la gráfica -->
<script src="assets/js/index_admin.js"></script>
<%@ include file="layout/footer.jsp" %>