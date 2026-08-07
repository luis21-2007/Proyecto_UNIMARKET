<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />

<%@ include file="layout/header.jsp" %>

<link href="assets/css/perfil-estilos.css" rel="stylesheet">

<div class="container-fluid py-4 profile-main-container">
    <div class="container">

        <!-- Mensajes de Feedback -->
        <c:if test="${param.msg == 'ok'}">
            <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
                Respuesta a la oferta registrada correctamente.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${param.msg == 'error'}">
            <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
                Ocurrió un error al procesar la oferta.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <div class="row g-4">

            <!-- PANEL LATERAL DE MENÚ -->
            <div class="col-md-4 col-lg-3">
                <div class="profile-sidebar-card shadow-sm">
                    <div class="profile-sidebar-title ms-2">Perfil</div>

                    <nav class="d-flex flex-column">
                        <a href="perfil" class="profile-menu-item">
                            <i class="bi bi-person"></i> Ajustes
                        </a>
                        <a href="#" class="profile-menu-item">
                            <i class="bi bi-bag-check"></i> Mis ventas
                        </a>
                        <a href="#" class="profile-menu-item">
                            <i class="bi bi-cart3"></i> Mis compras
                        </a>
                        <a href="#" class="profile-menu-item">
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

            <!-- CONTENIDO PRINCIPAL: OFERTAS RECIBIDAS -->
            <div class="col-md-8 col-lg-9 ps-md-4">

                <h1 class="profile-content-title mb-1">Ofertas Recibidas</h1>
                <p class="text-muted fw-semibold small mb-4">Administra las propuestas de compra que los usuarios han enviado por tus productos.</p>

                <div class="ofertas-container">
                    <c:choose>
                        <c:when test="${not empty listaOfertasRecibidas}">
                            <c:forEach var="oferta" items="${listaOfertasRecibidas}">

                                <!-- Tarjeta de Oferta Recibida -->
                                <div class="card border-0 shadow-sm rounded-3 mb-3 p-3 bg-white">
                                    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">

                                        <!-- Detalles de la Oferta y Comprador -->
                                        <div>
                                            <span class="badge bg-light text-dark border mb-1">
                                                Producto: <c:out value="${oferta.nombreProducto}"/>
                                            </span>
                                            <h5 class="fw-bold text-dark mb-1">
                                                Propuesta: <span class="text-success"><fmt:formatNumber value="${oferta.montoOferta}" type="currency" currencySymbol="$"/></span>
                                            </h5>
                                            <small class="text-secondary d-block">
                                                <i class="bi bi-person-circle me-1"></i>Comprador: <strong><c:out value="${oferta.nombreComprador}"/></strong>
                                            </small>
                                        </div>

                                        <!-- Estado y Acciones de Aceptar / Rechazar -->
                                        <div class="text-md-end">
                                            <c:choose>
                                                <%-- ESTADO 0: PENDIENTE -> MOSTRAR BOTONES DE ACCIÓN --%>
                                                <c:when test="${oferta.estado == 0}">
                                                    <div class="d-flex gap-2">
                                                        <a href="responderOferta?id=${oferta.idOferta}&accion=aceptar"
                                                           class="btn btn-success btn-sm px-3 fw-bold">
                                                            <i class="bi bi-check-lg me-1"></i> Aceptar
                                                        </a>
                                                        <a href="responderOferta?id=${oferta.idOferta}&accion=rechazar"
                                                           class="btn btn-outline-danger btn-sm px-3 fw-bold">
                                                            <i class="bi bi-x-lg me-1"></i> Rechazar
                                                        </a>
                                                    </div>
                                                </c:when>

                                                <%-- ESTADO 1: ACEPTADA --%>
                                                <c:when test="${oferta.estado == 1}">
                                                    <span class="badge bg-success px-3 py-2 rounded-pill fs-6">
                                                        <i class="bi bi-check-circle-fill me-1"></i> Aceptada
                                                    </span>
                                                </c:when>

                                                <%-- ESTADO 2: RECHAZADA --%>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary px-3 py-2 rounded-pill fs-6">
                                                        <i class="bi bi-x-circle me-1"></i> Rechazada
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                    </div>
                                </div>

                            </c:forEach>
                        </c:when>

                        <%-- Si no hay ofertas recibidas --%>
                        <c:otherwise>
                            <div class="text-center py-5 bg-white rounded-3 shadow-sm p-4">
                                <i class="bi bi-inbox fs-1 text-muted"></i>
                                <h5 class="fw-bold text-dark mt-3">Sin ofertas recibidas</h5>
                                <p class="text-muted small mb-0">No tienes ofertas pendientes de otros compradores por el momento.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>

        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>