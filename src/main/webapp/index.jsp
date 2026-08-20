<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="layout/header-index.jsp" %>

<form id="formFiltroCategoria" action="inicio" method="POST" style="display: none;">
    <input type="hidden" name="idCategoria" id="inputCategoriaId">
</form>

<c:if test="${param.msg == 'exito'}">
    <div class="row px-md-4 mb-3">
        <div class="col-12">
            <div class="alert alert-success alert-dismissible fade show d-flex align-items-center shadow-sm" role="alert" style="border-radius: 10px;">
                <i class="bi bi-check-circle-fill fs-4 me-2"></i>
                <div>
                    <strong>¡Excelente!</strong> Tu producto ha sido publicado con éxito en el marketplace.
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
    </div>
</c:if>

<div class="container-fluid py-3 main-market-container">
    <!-- TEXTO DE BIENVENIDA -->
    <div class="row mb-4">
        <div class="col-12 text-center">
            <p class="welcome-banner mb-0">
                ¡Bienvenido a Marketplace Universitario de Artículos! Disfruta de una gran variedad de productos en venta por parte de los alumnos de la UTEZ
            </p>
        </div>
    </div>
    <br>
    <br>
    <div class="mb-5 px-md-4">
        <div class="row mb-3">
            <div class="col-12">
                <h4 class="fw-bold text-dark d-flex align-items-center gap-2 mb-0">
                    <i class="bi bi-grid-fill text-warning"></i> Categorías
                </h4>
            </div>
        </div>
        <div class="row align-items-center position-relative">
            <div class="col-11">
                <div class="category-scroll-container">
                    <c:forEach var="cat" items="${listaCategorias}">
                        <a href="javascript:void(0);" onclick="filtrarPorCategoria('${cat.idCategoria}')" class="category-card text-center">
                            <img src="assets/img/Categoria_logo.png" alt="${cat.nombreCategoria}">
                            <div class="fw-bold small">${cat.nombreCategoria}</div>
                        </a>
                    </c:forEach>
                </div>
            </div>
            <div class="px-md-4">
                <br>
                <br>
                <c:choose>
                    <c:when test="${not empty listaProductos}">
                        <!-- CUADRÍCULA DE PRODUCTOS (Solo se activa si hay productos) -->
                        <div class="row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-5 row-cols-xl-6 g-4">
                            <c:forEach var="prod" items="${listaProductos}">
                                <div class="col">
                                    <div class="product-card">
                                        <div class="card-img-wrapper">
                                            <img src="${not empty prod.imagenUrl ? prod.imagenUrl : 'assets/img/icono-integradora.jpeg'}" alt="${prod.nombre}">
                                        </div>
                                        <div class="product-title text-truncate" title="${prod.nombre}">${prod.nombre}</div>
                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                            <span class="product-price">
                                                <fmt:formatNumber value="${prod.precio}" type="currency" currencySymbol="$"/> c/u
                                            </span>
                                        </div>

                                        <!-- CONTROL DE ACCESO AL DETALLE DEL PRODUCTO -->
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.usuario}">
                                                <a href="detalleProducto?id=${prod.idProducto}" class="btn btn-comprar shadow-sm w-100 text-center">
                                                    Ver detalle
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="login.jsp" class="btn btn-comprar shadow-sm w-100 text-center">
                                                    Ver detalle
                                                </a>
                                            </c:otherwise>
                                        </c:choose>

                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <div id="sinResultadosBusqueda" class="row d-none">
                            <div class="col-12 d-flex flex-column align-items-center justify-content-center text-center py-5 w-100" style="min-height: 30vh;">
                                <i class="bi bi-search display-1 text-muted opacity-50 mb-3"></i>
                                <p class="text-muted fs-5 fw-medium mb-0">No se encontraron productos que coincidan con tu búsqueda.</p>
                            </div>
                        </div>
                    </c:when>
                    <%-- MENSAJE CENTRADO CUANDO NO HAY PRODUCTOS --%>
                    <c:otherwise>
                        <div class="row">
                            <div class="col-12 d-flex flex-column align-items-center justify-content-center text-center py-5 w-100" style="min-height: 40vh;">
                                <i class="bi bi-box-seam display-1 text-muted opacity-50 mb-3"></i>
                                <p class="text-muted fs-5 fw-medium mb-0">No hay productos disponibles por el momento.</p>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<script src="assets/js/buscador-index.js"></script>

<%@ include file="layout/footer.jsp" %>