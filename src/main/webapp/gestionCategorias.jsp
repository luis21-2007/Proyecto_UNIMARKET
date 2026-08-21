<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="layout/header_admin.jsp" %>

<div class="container py-4">

    <!-- Título y Botón Nueva Categoría con clase aislada -->
    <div class="gestion-header-bar d-flex justify-content-between align-items-center mb-3">
        <h1 class="gestion-title mb-0">Gestión de Categorías</h1>
        <a href="agregarCategoria.jsp" class="btn btn-new-category rounded-pill px-4 fw-bold shadow-sm">
            <i class="bi bi-plus-lg me-1"></i> Nueva Categoría
        </a>
    </div>

    <!-- Mensajes de Alerta/Feedback -->
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
            <c:choose>
                <c:when test="${param.msg == 'creada'}">Categoría creada exitosamente.</c:when>
                <c:when test="${param.msg == 'actualizada'}">Categoría actualizada exitosamente.</c:when>
                <c:when test="${param.msg == 'desactivada'}">Categoría desactivada exitosamente.</c:when>
                <c:when test="${param.msg == 'activada'}">Categoría activada exitosamente.</c:when>
            </c:choose>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
            Ocurrió un error al procesar la solicitud.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Filtro de Búsqueda -->
    <div class="search-input-group d-flex align-items-center w-100 mb-3">
        <i class="bi bi-search text-secondary me-2 fs-5"></i>
        <input type="text" id="filtroCategoria" class="w-100" placeholder="Filtro de Categorías">
    </div>

    <!-- Contenedor Amarillo Principal -->
    <div class="users-container-card">

        <c:choose>
            <c:when test="${not empty listaCategorias}">
                <c:forEach var="cat" items="${listaCategorias}">
                    <!-- Tarjeta de Categoría -->
                    <div class="user-item-card categoria-item-card d-flex align-items-center justify-content-between mb-2 p-3 bg-white rounded-3 shadow-sm"
                         data-nombre="${fn:toLowerCase(cat.nombreCategoria)}">

                        <!-- 1. ICONO Y NOMBRE DE CATEGORÍA (IZQUIERDA) -->
                        <div class="d-flex align-items-center gap-2 flex-grow-1 overflow-hidden">
                            <div class="user-avatar" style="background-color: #e9ecef; border-color: #ced4da; color: #495057;">
                                <i class="bi bi-grid-fill fs-5"></i>
                            </div>

                            <div class="text-truncate">
                                <h6 class="mb-0 fw-bold text-dark text-truncate"><c:out value="${cat.nombreCategoria}"/></h6>
                                <small class="text-muted fw-semibold">
                                    Productos: <span class="badge bg-light text-dark border">${cat.totalProductos}</span>
                                </small>
                            </div>
                        </div>

                        <!-- 2. ESTADO Y OPCIONES (DERECHA) -->
                        <div class="d-flex align-items-center justify-content-end gap-2 ms-auto">
                            <!-- Badge de Estado Activa/Inactiva -->
                            <c:choose>
                                <c:when test="${cat.estado}">
                                    <span class="status-badge-active">Activa</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge-inactive">Inactiva</span>
                                </c:otherwise>
                            </c:choose>

                            <!-- Menú Opciones (Tres Puntos) -->
                            <div class="dropdown">
                                <button class="btn-options" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="bi bi-three-dots"></i>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end shadow">
                                    <li>
                                        <a class="dropdown-item" href="javascript:void(0)" onclick="prepararModalEditar('${cat.idCategoria}', '${fn:escapeXml(cat.nombreCategoria)}')">
                                            <i class="bi bi-pencil-square me-2 text-primary"></i>Editar
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                    <c:choose>
                                        <c:when test="${cat.estado}">
                                            <li>
                                                <a class="dropdown-item text-danger" href="javascript:void(0)" onclick="prepararModalEstado('${cat.idCategoria}', '${fn:escapeXml(cat.nombreCategoria)}', 'desactivar')">
                                                    <i class="bi bi-slash-circle me-2"></i>Deshabilitar
                                                </a>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li>
                                                <a class="dropdown-item text-success" href="javascript:void(0)" onclick="prepararModalEstado('${cat.idCategoria}', '${fn:escapeXml(cat.nombreCategoria)}', 'activar')">
                                                    <i class="bi bi-check-circle me-2"></i>Activar
                                                </a>
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
                    <i class="bi bi-grid-3x3-gap fs-1 text-muted"></i>
                    <p class="text-muted fw-semibold mb-0 mt-2">No hay categorías registradas en la base de datos.</p>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

    <!-- Paginación -->
    <nav class="d-flex justify-content-center mt-4" id="navPaginacionCat">
        <ul class="pagination pagination-lg mb-0" id="paginacionContainerCat">
        </ul>
    </nav>

</div>

<!-- Modal de Confirmación Adaptativo (Activar/Desactivar) -->
<div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content custom-modal-content">
            <div class="modal-body p-4 text-center">
                <p class="modal-text mb-0">
                    Estás por <span id="textoAccionModal"></span> la categoría <strong id="categoriaNombreConfirm"></strong>.<br>¿Estás seguro?
                </p>
                <div class="d-flex justify-content-between mt-4 px-2">
                    <button type="button" class="btn btn-cancelar" data-bs-dismiss="modal">Cancelar</button>
                    <a href="#" id="btnConfirmAction" class="btn btn-confirmar">Confirmar</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal de Editar Categoría -->
<div class="modal fade" id="editCategoriaModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content edit-modal-content">
            <div class="modal-body p-4">
                <h4 class="edit-modal-title mb-4">Editar Categoría</h4>

                <form id="editCategoriaForm" action="categorias" method="POST">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" id="editCategoriaId" name="categoriaId">

                    <div class="mb-3 text-start">
                        <label for="editNombreCategoria" class="edit-field-label">Nombre Categoría</label>
                        <div class="edit-input-group">
                            <input type="text" id="editNombreCategoria" name="nombre" class="edit-input w-100" required>
                        </div>
                    </div>
                    <div class="d-flex justify-content-between mt-4">
                        <button type="button" class="btn btn-cancelar-edit" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-guardar-edit">Guardar Cambios</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="assets/js/gestion_categorias.js"></script>
<script src="assets/js/paginador-categorias.js"></script>

<%@ include file="layout/footer.jsp" %>