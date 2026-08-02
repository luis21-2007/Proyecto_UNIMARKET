<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="layout/header.jsp" %>

<!-- FORMULARIO OCULTO PARA ENVIAR POR POST SIN MOSTRAR '?' EN LA URL -->
<form id="formFiltroCategoria" action="inicio" method="POST" style="display: none;">
    <input type="hidden" name="categoriaId" id="inputCategoriaId">
</form>

<div class="container-fluid py-3 main-market-container">
    <!-- TEXTO DE BIENVENIDA -->
    <div class="row mb-4">
        <div class="col-12 text-center">
            <p class="welcome-banner mb-0">
                ¡Bienvenido a Marketplace Universitario de Artículos! Disfruta de una gran variedad de productos en venta por parte de los alumnos de la UTEZ
            </p>
        </div>
    </div>

    <!-- SECCIÓN DE CATEGORÍAS HORIZONTALES DINÁMICAS -->
    <div class="row align-items-center mb-5 position-relative px-md-4">
        <div class="col-11">
            <div class="category-scroll-container">
                <c:forEach var="cat" items="${listaCategorias}">
                    <!-- Cambiamos href="inicio?categoriaId=..." por la función JavaScript -->
                    <a href="javascript:void(0);" onclick="filtrarPorCategoria('${cat.idCategoria}')" class="category-card text-center">
                        <img src="assets/img/icono-integradora.jpeg" alt="${cat.nombreCategoria}">
                        <div class="fw-bold small">${cat.nombreCategoria}</div>
                    </a>
                </c:forEach>
            </div>
        </div>
        <!-- Flecha de navegación derecha -->
        <div class="col-1 text-end d-none d-md-block">
            <i class="bi bi-chevron-right fs-2 fw-bold text-dark cursor-pointer" style="cursor: pointer;"></i>
        </div>
    </div>
    <!-- CUADRÍCULA DE PRODUCTOS DINÁMICA -->
    <div class="px-md-4">
        <div class="row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-5 row-cols-xl-6 g-4">

            <c:choose>
                <c:when test="${not empty listaProductos}">
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
                                    <span class="product-rating"><i class="bi bi-star-fill me-1"></i>5.0</span>
                                </div>

                                <!-- CONTROL DE ACCESO AL DETALLE DEL PRODUCTO -->
                                <c:choose>
                                    <%-- Si el usuario inició sesión, va al detalle del producto --%>
                                    <c:when test="${not empty sessionScope.usuario}">
                                        <a href="detalleProducto?id=${prod.idProducto}" class="btn btn-comprar shadow-sm w-100 text-center">
                                            Ver detalle
                                        </a>
                                    </c:when>
                                    <%-- Si es visitante, lo redirige al Login --%>
                                    <c:otherwise>
                                        <a href="login.jsp" class="btn btn-comprar shadow-sm w-100 text-center">
                                            Ver detalle
                                        </a>
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-box-seam fs-1 text-muted"></i>
                        <p class="text-muted mt-2">No hay productos disponibles por el momento.</p>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </div>

</div>
<%@ include file="layout/footer.jsp" %>