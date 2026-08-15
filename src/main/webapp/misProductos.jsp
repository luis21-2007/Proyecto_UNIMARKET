<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />

<%@ include file="layout/header.jsp" %>
<link href="assets/css/mis-productos.css" rel="stylesheet">

<div class="container-fluid py-4 main-market-container">

    <c:if test="${param.msg == 'exito'}">
        <div class="row px-md-4 mb-3">
            <div class="col-12">
                <div class="alert alert-success alert-dismissible fade show d-flex align-items-center shadow-sm" role="alert" style="border-radius: 10px;">
                    <i class="bi bi-check-circle-fill fs-4 me-2"></i>
                    <div>
                        <strong>¡Excelente!</strong> Tu producto ha sido editado con éxito.
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${param.msg == 'vendidoExitoso'}">
        <div class="row px-md-4 mb-3">
            <div class="col-12">
                <div class="alert alert-info alert-dismissible fade show d-flex align-items-center shadow-sm" role="alert" style="border-radius: 10px;">
                    <i class="bi bi-check2-circle fs-4 me-2"></i>
                    <div>
                        <strong>¡Felicidades por tu venta!</strong> El producto se ha marcado como vendido y se movió a tu historial.
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${param.msg == 'eliminadoExitoso'}">
        <div class="row px-md-4 mb-3">
            <div class="col-12">
                <div class="alert alert-warning alert-dismissible fade show d-flex align-items-center shadow-sm" role="alert" style="border-radius: 10px;">
                    <i class="bi bi-trash-fill fs-4 me-2"></i>
                    <div>
                        El producto se ha dado de baja correctamente y ahora se encuentra en tu historial de eliminados.
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </div>
    </c:if>

    <div class="row mb-4">
        <div class="col-12 text-center">
            <h2 class="fw-bold text-dark">Mis Productos</h2>
            <p class="welcome-banner  mb-0">
                Gestiona tus publicaciones activas, revisa tu historial de ventas y consulta tus productos eliminados.
            </p>
        </div>
    </div>

    <!-- NAVEGACIÓN POR PESTAÑAS (TABS) -->
    <div class="px-md-4">
        <ul class="nav nav-pills justify-content-center mb-4 gap-2" id="productosTab" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active fw-bold px-4 rounded-pill" id="activos-tab" data-bs-toggle="pill" data-bs-target="#activos" type="button" role="tab">
                    <i class="bi bi-box-seam me-1"></i> Publicados
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link fw-bold px-4 rounded-pill" id="vendidos-tab" data-bs-toggle="pill" data-bs-target="#vendidos" type="button" role="tab">
                    <i class="bi bi-check2-circle me-1"></i> Vendidos
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link fw-bold px-4 rounded-pill" id="eliminados-tab" data-bs-toggle="pill" data-bs-target="#eliminados" type="button" role="tab">
                    <i class="bi bi-x-circle me-1"></i> Eliminados
                </button>
            </li>
        </ul>
        <div class="tab-content" id="productosTabContent">

            <div class="tab-pane fade show active" id="activos" role="tabpanel">
                <c:set var="tieneActivos" value="false" />
                <div class="row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-5 row-cols-xl-6 g-4">
                    <c:forEach var="prod" items="${listaProductos}">
                        <c:if test="${prod.estado == 1}">
                            <c:set var="tieneActivos" value="true" />
                            <div class="col">
                                <div class="product-card h-100 d-flex flex-column justify-content-between">
                                    <div>
                                        <div class="card-img-wrapper position-relative">
                                            <img src="${not empty prod.imagenUrl ? prod.imagenUrl : 'assets/img/icono-integradora.jpeg'}" alt="${prod.nombre}">
                                            <span class="badge bg-success position-absolute top-0 end-0 m-2">Activo</span>
                                        </div>
                                        <div class="product-title text-truncate mt-2" title="${prod.nombre}">${prod.nombre}</div>
                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                            <span class="product-price">
                                                <fmt:formatNumber value="${prod.precio}" type="currency" currencySymbol="$"/>
                                            </span>
                                        </div>
                                    </div>
                                    <a href="detallemiProducto?id=${prod.idProducto}" class="btn btn-comprar shadow-sm w-100 text-center mt-2">
                                        <i class="bi bi-pencil-square me-1"></i> Editar
                                    </a>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>

                <c:if test="${!tieneActivos}">
                    <div class="text-center py-5">
                        <i class="bi bi-box-seam display-1 text-muted opacity-50 d-block mb-3"></i>
                        <p class="text-muted fs-5 fw-medium mb-0">No tienes productos activos actualmente.</p>
                    </div>
                </c:if>
            </div>

            <!-- 2. PESTAÑA: PRODUCTOS VENDIDOS (estado == 2) -->
            <div class="tab-pane fade" id="vendidos" role="tabpanel">
                <c:set var="tieneVendidos" value="false" />
                <div class="row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-5 row-cols-xl-6 g-4">
                    <c:forEach var="prod" items="${listaProductos}">
                        <c:if test="${prod.estado == 2}">
                            <c:set var="tieneVendidos" value="true" />
                            <div class="col">
                                <div class="product-card h-100 d-flex flex-column justify-content-between border-success">
                                    <div>
                                        <div class="card-img-wrapper position-relative">
                                            <img src="${not empty prod.imagenUrl ? prod.imagenUrl : 'assets/img/icono-integradora.jpeg'}" alt="${prod.nombre}">
                                            <span class="badge bg-danger position-absolute top-0 end-0 m-2">Vendido</span>
                                        </div>
                                        <div class="product-title text-truncate mt-2" title="${prod.nombre}">${prod.nombre}</div>
                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                            <span class="product-price text-success fw-bold">
                                                <fmt:formatNumber value="${prod.precio}" type="currency" currencySymbol="$"/>
                                            </span>
                                        </div>
                                    </div>
                                    <button class="btn btn-secondary shadow-sm w-100 text-center mt-2" disabled style="opacity: 0.75;">
                                        <i class="bi bi-check-circle-fill me-1"></i> Vendido
                                    </button>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>

                <c:if test="${!tieneVendidos}">
                    <div class="text-center py-5">
                        <i class="bi bi-bag-check display-1 text-muted opacity-50 d-block mb-3"></i>
                        <p class="text-muted fs-5 fw-medium mb-0">Aún no has vendido ningún producto.</p>
                    </div>
                </c:if>
            </div>

            <!-- 3. PESTAÑA: PRODUCTOS ELIMINADOS (estado == 0) -->
            <div class="tab-pane fade" id="eliminados" role="tabpanel">
                <c:set var="tieneEliminados" value="false" />
                <div class="row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-5 row-cols-xl-6 g-4">
                    <c:forEach var="prod" items="${listaProductos}">
                        <c:if test="${prod.estado == 0}">
                            <c:set var="tieneEliminados" value="true" />
                            <div class="col">
                                <div class="product-card h-100 d-flex flex-column justify-content-between opacity-75" style="background-color: #f8f9fa;">
                                    <div>
                                        <div class="card-img-wrapper position-relative">
                                            <img src="${not empty prod.imagenUrl ? prod.imagenUrl : 'assets/img/icono-integradora.jpeg'}" alt="${prod.nombre}" style="filter: grayscale(80%);">
                                            <span class="badge bg-danger position-absolute top-0 end-0 m-2">Eliminado</span>
                                        </div>
                                        <div class="product-title text-truncate text-muted mt-2" title="${prod.nombre}">${prod.nombre}</div>
                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                            <span class="product-price text-muted">
                                                <fmt:formatNumber value="${prod.precio}" type="currency" currencySymbol="$"/>
                                            </span>
                                        </div>
                                    </div>
                                    <button class="btn btn-outline-secondary shadow-sm w-100 text-center mt-2" disabled>
                                        <i class="bi bi-slash-circle me-1"></i> Dado de baja
                                    </button>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>

                <c:if test="${!tieneEliminados}">
                    <div class="text-center py-5">
                        <i class="bi bi-trash display-1 text-muted opacity-50 d-block mb-3"></i>
                        <p class="text-muted fs-5 fw-medium mb-0">No tienes productos en tu historial de eliminados.</p>
                    </div>
                </c:if>
            </div>

        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>