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
                        <a href="misVentas" class="profile-menu-item active">
                            <i class="bi bi-bag-check"></i> Mis ventas
                        </a>
                        <a href="misCompras" class="profile-menu-item">
                            <i class="bi bi-cart3"></i> Mis compras
                        </a>
                        <a href="misResenas" class="profile-menu-item">
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

                <!-- Muestra mensaje de éxito si se actualizó el estado -->
                <c:if test="${param.msg == 'estadoActualizado'}">
                    <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i> El estado de la transacción ha sido actualizado.
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

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

                                        <div class="d-flex align-items-center gap-3">
                                            <!-- BADGE DINÁMICO DE ESTADO -->
                                            <c:choose>
                                                <c:when test="${venta.estado == 1}">
                                                    <span class="badge bg-success px-3 py-2 rounded-pill">
                                                        <i class="bi bi-bag-check-fill me-1"></i> Vendido / Completada
                                                    </span>
                                                </c:when>
                                                <c:when test="${venta.estado == 2}">
                                                    <span class="badge bg-warning text-dark px-3 py-2 rounded-pill">
                                                        <i class="bi bi-clock-history me-1"></i> En Proceso
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger px-3 py-2 rounded-pill">
                                                        <i class="bi bi-x-circle-fill me-1"></i> Cancelada
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>

                                            <!-- MENÚ DE 3 PUNTOS PARA CAMBIAR ESTADO -->
                                            <div class="dropdown">
                                                <button class="btn btn-light btn-sm rounded-circle shadow-none border-0" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                                    <i class="bi bi-three-dots-vertical fs-5"></i>
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0">
                                                    <c:if test="${venta.estado != 1}">
                                                        <li>
                                                            <form action="actualizarEstadoTransaccion" method="POST">
                                                                <input type="hidden" name="idTransaccion" value="${venta.idTransaccion}">
                                                                <input type="hidden" name="nuevoEstado" value="1">
                                                                <button type="submit" class="dropdown-item text-success fw-semibold">
                                                                    <i class="bi bi-check-circle-fill me-2"></i>Marcar como Vendido
                                                                </button>
                                                            </form>
                                                        </li>
                                                    </c:if>

                                                    <c:if test="${venta.estado != 0}">
                                                        <c:if test="${venta.estado != 1}">
                                                            <li><hr class="dropdown-divider"></li>
                                                        </c:if>
                                                        <li>
                                                            <form action="actualizarEstadoTransaccion" method="POST">
                                                                <input type="hidden" name="idTransaccion" value="${venta.idTransaccion}">
                                                                <input type="hidden" name="nuevoEstado" value="0">
                                                                <button type="submit" class="dropdown-item text-danger fw-semibold">
                                                                    <i class="bi bi-x-circle-fill me-2"></i>Cancelar Transacción
                                                                </button>
                                                            </form>
                                                        </li>
                                                    </c:if>
                                                </ul>
                                            </div>
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