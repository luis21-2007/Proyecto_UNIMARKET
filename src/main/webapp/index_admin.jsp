<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.maqueta_integradora.model.User" %>
<%
    User userAdmin = (User) session.getAttribute("usuario");
    if (userAdmin == null || !"ADMIN".equals(userAdmin.getRol())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="layout/header_admin.jsp" %>


<div class="container-fluid px-4 px-md-5 mb-5">
    <h1 class="page-title">Administrador</h1>

    <!-- FILA DE TARJETAS (CARDS) BOTONES -->
    <div class="row g-4 justify-content-center">

        <div class="col-12 col-md-6">
            <a href="gestionProductos" class="stat-card-link text-decoration-none">
                <div class="stat-card">
                    <div class="stat-title">Productos Activos</div>
                    <div class="d-flex justify-content-between align-items-end">
                        <div class="stat-value">${totalProductos}</div>
                        <div class="stat-icon"><i class="bi bi-box-seam"></i></div>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-12 col-md-6">
            <a href="gestionUsuarios" class="stat-card-link text-decoration-none">
                <div class="stat-card">
                    <div class="stat-title">Total de usuarios</div>
                    <div class="d-flex justify-content-between align-items-end">
                        <div class="stat-value">${totalUsuarios}</div>
                        <div class="stat-icon"><i class="bi bi-person-fill"></i></div>
                    </div>
                </div>
            </a>
        </div>

    </div>

    <h2 class="chart-section-title mt-5">Grafica de Productos Y Usuarios</h2>

    <div class="row">
        <div class="col-12">
            <div class="chart-card">
                <div style="height: 350px; width: 100%; margin-top: 15px;">
                    <canvas id="productosChart"
                            data-usuarios="${totalUsuarios}"
                            data-productos="${totalProductos}">
                    </canvas>
                </div>
            </div>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="assets/js/index_admin.js"></script>
<%@ include file="layout/footer.jsp" %>