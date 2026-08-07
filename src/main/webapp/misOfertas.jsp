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


            <!-- CONTENIDO PRINCIPAL: MIS OFERTAS -->
            <div class="col-md-8 col-lg-9 ps-md-4">

                <h1 class="profile-content-title mb-1">Mis Ofertas</h1>
                <p class="text-muted fw-semibold small mb-4">Consulta el estado de las ofertas que has enviado a los vendedores.</p>

                <div class="ofertas-container">
                    <c:choose>
                        <c:when test="${not empty listaOfertas}">
                            <c:forEach var="oferta" items="${listaOfertas}">

                                <!-- Tarjeta de Oferta -->
                                <div class="card border-0 shadow-sm rounded-3 mb-3 p-3 bg-white">
                                    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">

                                        <!-- Detalles de la Oferta -->
                                        <div>
                                            <h5 class="fw-bold text-dark mb-1">
                                                <c:out value="${oferta.nombreProducto}"/>
                                            </h5>
                                            <p class="mb-1 text-muted fw-semibold">
                                                Tu oferta propuesta:
                                                <span class="text-success fw-bold">
                                                    <fmt:formatNumber value="${oferta.montoOferta}" type="currency" currencySymbol="$"/>
                                                </span>
                                            </p>
                                            <small class="text-secondary d-block">
                                                <i class="bi bi-shop me-1"></i>Vendedor: <strong><c:out value="${oferta.nombreVendedor}"/></strong>
                                            </small>
                                        </div>

                                        <!-- Sección de Estado y WhatsApp en misOfertas.jsp -->
                                        <div class="text-md-end">
                                            <c:choose>
                                                <%-- ESTADO 0: PENDIENTE --%>
                                                <c:when test="${oferta.estado == 0}">
                                                    <span class="badge bg-warning text-dark px-3 py-2 rounded-pill fs-6">
                                                        <i class="bi bi-clock-history me-1"></i> Pendiente
                                                    </span>
                                                </c:when>

                                                <%-- ESTADO 1: ACEPTADA -> MUESTRA BADGE Y ENLACE DE WHATSAPP --%>
                                                <c:when test="${oferta.estado == 1}">
                                                    <div class="d-flex flex-column align-items-md-end gap-2">
                                                        <span class="badge bg-success px-3 py-2 rounded-pill fs-6">
                                                            <i class="bi bi-check-circle-fill me-1"></i> Aceptada
                                                        </span>

                                                        <a href="https://wa.me/${oferta.telefonoVendedor}?text=Hola%20${oferta.nombreVendedor},%20mi%20oferta%20de%20$${oferta.montoOferta}%20por%20${oferta.nombreProducto}%20fue%20aceptada."
                                                           target="_blank"
                                                           class="btn btn-success btn-sm px-3 fw-bold shadow-sm d-inline-flex align-items-center gap-1">
                                                            <i class="bi bi-whatsapp fs-6"></i> Contactar por WhatsApp
                                                        </a>
                                                    </div>
                                                </c:when>

                                                <%-- ESTADO 2: RECHAZADA --%>
                                                <c:otherwise>
                                                    <span class="badge bg-danger px-3 py-2 rounded-pill fs-6">
                                                        <i class="bi bi-x-circle me-1"></i> Rechazada
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>

                            </c:forEach>
                        </c:when>

                        <%-- Si el usuario no tiene ofertas creadas --%>
                        <c:otherwise>
                            <div class="text-center py-5 bg-white rounded-3 shadow-sm p-4">
                                <i class="bi bi-tag fs-1 text-muted"></i>
                                <h5 class="fw-bold text-dark mt-3">Aún no has realizado ninguna oferta</h5>
                                <p class="text-muted small mb-3">Explora los productos del catálogo y envía una propuesta a los vendedores.</p>
                                <a href="inicio" class="btn btn-outline-primary btn-sm rounded-pill px-4">Ir a Explorar</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>

        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>