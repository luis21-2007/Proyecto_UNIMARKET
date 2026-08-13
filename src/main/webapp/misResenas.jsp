<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />

<%@ include file="layout/header.jsp" %>

<link href="assets/css/perfil-estilos.css" rel="stylesheet">


<div class="container-fluid py-4 profile-main-container">
    <div class="container">

        <div class="row g-4">

            <!-- PANEL LATERAL DE MENÚ -->
            <div class="col-md-4 col-lg-3">
                <div class="profile-sidebar-card shadow-sm">
                    <div class="profile-sidebar-title ms-2">Perfil</div>

                    <nav class="d-flex flex-column">
                        <a href="perfil" class="profile-menu-item">
                            <i class="bi bi-person"></i> Ajustes
                        </a>
                        <a href="misVentas" class="profile-menu-item">
                            <i class="bi bi-bag-check"></i> Mis ventas
                        </a>
                        <a href="misCompras" class="profile-menu-item">
                            <i class="bi bi-cart3"></i> Mis compras
                        </a>
                        <a href="misResenas" class="profile-menu-item active">
                            <i class="bi bi-star"></i> Mis reseñas
                        </a>
                        <a href="misOfertas" class="profile-menu-item">
                            <i class="bi bi-tag"></i> Mis ofertas enviadas
                        </a>
                        <a href="misOfertasVendedor" class="profile-menu-item">
                            <i class="bi bi-inbox"></i> Ofertas recibidas
                        </a>
                    </nav>
                </div>
            </div>

            <!-- CONTENIDO PRINCIPAL: MIS RESEÑAS -->
            <div class="col-md-8 col-lg-9 ps-md-4">

                <h1 class="profile-content-title mb-1">Mis Reseñas</h1>
                <p class="text-muted fw-semibold small mb-4">Opiniones y calificaciones recibidas por compradores de tus productos.</p>

                <c:choose>
                    <c:when test="${not empty listaResenas}">

                        <!-- TARJETA DE RESUMEN PROMEDIO DE CALIFICACIÓN -->
                        <div class="card border-0 shadow-sm rounded-3 p-4 mb-4 bg-white">
                            <div class="d-flex flex-column flex-sm-row align-items-center gap-3 text-center text-sm-start">
                                <div class="display-4 fw-bold text-dark mb-0">
                                    <fmt:formatNumber value="${promedioCalificaciones}" maxFractionDigits="1" minFractionDigits="1" />
                                </div>
                                <div>
                                    <div class="text-warning fs-4 mb-1">
                                        <c:forEach var="i" begin="1" end="5">
                                            <c:choose>
                                                <c:when test="${i <= promedioCalificaciones}">
                                                    <i class="bi bi-star-fill"></i>
                                                </c:when>
                                                <c:when test="${i - 0.5 <= promedioCalificaciones}">
                                                    <i class="bi bi-star-half"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="bi bi-star"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                    <p class="text-muted small fw-semibold mb-0">
                                        Basado en <strong>${totalResenas}</strong> ${totalResenas == 1 ? 'reseña' : 'reseñas'} recibidas
                                    </p>
                                </div>
                            </div>
                        </div>

                        <!-- LISTA DE RESEÑAS -->
                        <div class="resenas-container">
                            <c:forEach var="resena" items="${listaResenas}">
                                <div class="card border-0 shadow-sm rounded-3 mb-3 p-3 bg-white">
                                    <div class="d-flex justify-content-between align-items-start mb-2">

                                        <!-- Nombre Comprador y Producto -->
                                        <div>
                                            <h6 class="fw-bold text-dark mb-1">
                                                <i class="bi bi-person-circle me-1 text-secondary"></i>
                                                <c:out value="${resena.nombreComprador}" />
                                            </h6>
                                            <small class="text-muted fw-semibold">
                                                <i class="bi bi-box-seam me-1"></i>Producto:
                                                <span class="text-dark"><c:out value="${resena.nombreProducto}" /></span>
                                            </small>
                                        </div>

                                        <!-- Puntuación y Fecha -->
                                        <div class="text-end">
                                            <div class="text-warning fs-6 mb-1">
                                                <c:forEach var="i" begin="1" end="5">
                                                    <i class="bi ${i <= resena.puntuacion ? 'bi-star-fill' : 'bi-star'}"></i>
                                                </c:forEach>
                                            </div>
                                            <small class="text-secondary d-block" style="font-size: 0.8rem;">
                                                <fmt:formatDate value="${resena.fechaCalificacion}" pattern="dd/MM/yyyy HH:mm"/>
                                            </small>
                                        </div>
                                    </div>

                                    <!-- Comentario del comprador -->
                                    <div class="bg-light p-3 rounded-3 mt-2 border-start border-3 border-warning">
                                        <p class="mb-0 text-secondary fst-italic small">
                                            <c:choose>
                                                <c:when test="${not empty resena.comentario}">
                                                    "<c:out value="${resena.comentario}"/>"
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">El comprador no dejó un comentario escrito.</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                    </c:when>

                    <c:otherwise>
                        <%-- ESTADO VACÍO (SIN RESEÑAS) --%>
                        <div class="text-center py-5 bg-white rounded-3 shadow-sm p-4">
                            <i class="bi bi-star-slash fs-1 text-muted"></i>
                            <h5 class="fw-bold text-dark mt-3">Aún no tienes reseñas</h5>
                            <p class="text-muted small mb-0">Cuando vendas productos y los compradores te califiquen, sus opiniones aparecerán aquí.</p>
                        </div>
                    </c:otherwise>
                </c:choose>

            </div>

        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>