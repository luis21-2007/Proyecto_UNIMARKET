<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />

<%@ include file="layout/header_admin.jsp" %>

<div class="container py-4">
    <h1 class="gestion-title">Gestión de Productos</h1>

    <!-- Mensajes de Alerta / Feedback -->
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
            <c:choose>
                <c:when test="${param.msg == 'desactivado'}">Producto deshabilitado exitosamente.</c:when>
                <c:when test="${param.msg == 'activado'}">Producto activado exitosamente.</c:when>
            </c:choose>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
            Ocurrió un error al procesar la solicitud del producto.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Filtro de Búsqueda -->
    <div class="search-input-group d-flex align-items-center w-100 mb-3">
        <i class="bi bi-search text-secondary me-2 fs-5"></i>
        <input type="text" id="filtroProducto" class="w-100" placeholder="Filtro de Productos">
    </div>

    <div class="users-container-card">

        <c:choose>
            <c:when test="${not empty listaProductos}">
                <c:forEach var="prod" items="${listaProductos}">
                    <!-- Item de Producto -->
                    <div class="user-item-card d-flex align-items-center justify-content-between mb-2 p-3 bg-white rounded-3 shadow-sm"
                         data-nombre="${fn:toLowerCase(prod.nombre)}"
                         data-vendedor="${fn:toLowerCase(not empty prod.nombreVendedor ? prod.nombreVendedor : '')}">

                        <!-- 1. DETALLES DEL PRODUCTO (IZQUIERDA) -->
                        <div class="d-flex align-items-center gap-3" style="flex: 1;">
                            <!-- Miniatura -->
                            <div class="product-thumb-wrapper" style="width: 48px; height: 48px; border-radius: 10px; overflow: hidden; background-color: #f8f9fa;">
                                <img src="${not empty prod.imagenUrl ? prod.imagenUrl : 'assets/img/icono-integradora.jpeg'}"
                                     alt="<c:out value='${prod.nombre}'/>"
                                     style="width: 100%; height: 100%; object-fit: cover;">
                            </div>

                            <div>
                                <h6 class="mb-0 fw-bold text-dark"><c:out value="${prod.nombre}"/></h6>
                                <small class="text-muted fw-semibold">
                                    Precio: <span class="text-success fw-bold"><fmt:formatNumber value="${prod.precio}" type="currency" currencySymbol="$"/></span>
                                </small>
                            </div>
                        </div>

                        <!-- 2. VENDEDOR (COLUMNA EN EL MEDIO) -->
                        <div class="text-center px-3" style="flex: 1;">
                            <small class="text-muted d-block fw-semibold" style="font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.5px;">Vendedor</small>
                            <span class="fw-bold text-dark">
                                <c:out value="${not empty prod.nombreVendedor ? prod.nombreVendedor : 'Sin Asignar'}"/>
                            </span>
                        </div>

                        <!-- 3. ESTADO Y OPCIONES (DERECHA) -->
                        <div class="d-flex align-items-center justify-content-end gap-3" style="flex: 1;">
                            <!-- Badge de Estado (1: Activo, 2: Vendido, 0: Inactivo) -->
                            <c:choose>
                                <c:when test="${prod.estado == 1}">
                                    <span class="status-badge-active">Activo</span>
                                </c:when>
                                <c:when test="${prod.estado == 2}">
                                    <span class="badge bg-primary px-3 py-2 rounded-pill fs-7">
                                        <i class="bi bi-bag-check-fill me-1"></i>Vendido
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge-inactive">Inactivo</span>
                                </c:otherwise>
                            </c:choose>

                            <!-- Menú Opciones -->
                            <div class="dropdown">
                                <button class="btn-options" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="bi bi-three-dots"></i>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end shadow">
                                    <c:choose>
                                        <c:when test="${prod.estado == 1}">
                                            <li>
                                                <a class="dropdown-item text-danger"
                                                   href="javascript:void(0)"
                                                   onclick="prepararModalEstadoProducto(${prod.idProducto}, '${fn:escapeXml(prod.nombre)}', 'desactivar')">
                                                    <i class="bi bi-slash-circle me-2"></i>Deshabilitar
                                                </a>
                                            </li>
                                        </c:when>
                                        <c:when test="${prod.estado == 0}">
                                            <li>
                                                <a class="dropdown-item text-success"
                                                   href="javascript:void(0)"
                                                   onclick="prepararModalEstadoProducto(${prod.idProducto}, '${fn:escapeXml(prod.nombre)}', 'activar')">
                                                    <i class="bi bi-check-circle me-2"></i>Activar
                                                </a>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li>
                                                <span class="dropdown-item text-muted disabled">
                                                    <i class="bi bi-lock-fill me-2"></i>Sin acciones (Vendido)
                                                </span>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
                            </div>
                        </div>

                    </div>
                </c:forEach>
            </c:when>

            <c:otherwise>
                <div class="text-center py-4 bg-white rounded-3">
                    <i class="bi bi-box-seam fs-1 text-muted"></i>
                    <p class="text-muted fw-semibold mb-0 mt-2">No se encontraron productos registrados en la base de datos.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<nav class="d-flex justify-content-center mt-4" id="navPaginacion">
    <ul class="pagination pagination-lg mb-0" id="paginacionContainer">

    </ul>
</nav>

<!-- Modal de Confirmación Adaptativo (Activar/Desactivar) -->
<div class="modal fade" id="confirmModalProducto" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content custom-modal-content">
            <div class="modal-body p-4 text-center">
                <p class="modal-text mb-0">
                    Estás por <span id="accionTextoConfirmProd"></span> el producto <strong id="productoNombreConfirm"></strong>.<br>¿Estás seguro?
                </p>
                <div class="d-flex justify-content-between mt-4 px-2">
                    <button type="button" class="btn btn-cancelar" data-bs-dismiss="modal">Cancelar</button>
                    <a href="#" id="btnConfirmActionProducto" class="btn btn-confirmar">Confirmar</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="assets/js/gestion-productos.js"></script>ç
<script src="assets/js/paginador-usuarios.js"></script>
<%@ include file="layout/footer.jsp" %>