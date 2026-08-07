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

            <!-- CONTENIDO PRINCIPAL: MIS VENTAS -->
            <div class="col-md-8 col-lg-9 ps-md-4">
                <h1 class="profile-content-title mb-1">Mis Ventas</h1>
                <p class="text-muted fw-semibold small mb-4">Registro de ventas concretadas con los compradores.</p>

                <div class="ventas-container">
                    <c:choose>
                        <c:when test="${not empty listaVentas}">
                            <c:forEach var="venta" items="${listaVentas}">
                                <div class="card border-0 shadow-sm rounded-3 mb-3 p-3 bg-white">
                                    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
                                        <div>
                                            <h5 class="fw-bold text-dark mb-1"><c:out value="${venta.nombreProducto}"/></h5>
                                            <p class="mb-1 text-muted fw-semibold">
                                                Total cobrado:
                                                <span class="text-success fw-bold">
                                                    <fmt:formatNumber value="${venta.monto}" type="currency" currencySymbol="$"/>
                                                </span>
                                            </p>
                                            <small class="text-secondary d-block">
                                                <i class="bi bi-person-circle me-1"></i>Comprador: <strong><c:out value="${venta.nombreComprador}"/></strong>
                                                | Fecha: <fmt:formatDate value="${venta.fechaTransaccion}" pattern="dd/MM/yyyy HH:mm"/>
                                            </small>
                                        </div>

                                        <div class="text-md-end">
                                            <span class="badge bg-primary px-3 py-2 rounded-pill fs-6 mb-2 d-inline-block">
                                                <i class="bi bi-bag-check-fill me-1"></i> Venta Concretada
                                            </span>
                                            <br>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-5 bg-white rounded-3 shadow-sm p-4">
                                <i class="bi bi-bag-x fs-1 text-muted"></i>
                                <h5 class="fw-bold text-dark mt-3">Sin ventas registradas</h5>
                                <p class="text-muted small mb-0">Cuando aceptes la oferta de un comprador, tu venta se registrará automáticamente aquí.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>