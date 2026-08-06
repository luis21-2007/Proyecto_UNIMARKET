<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="layout/header_admin.jsp" %>

<div class="container mt-4 mb-5">

    <!-- Mensajes de Alerta/Feedback -->
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
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
        <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
            Ocurrió un error al procesar la solicitud.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Título y Botón Nueva Categoría -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="page-title">Gestión de Categorías</h1>
        <a href="agregarCategoria.jsp" class="btn btn-new-category">+ Nueva Categoría</a>
    </div>

    <!-- Tabla de Categorías -->
    <div class="table-container shadow-sm">
        <div class="table-header-main">
            Todas las Categorías
        </div>
        <table class="table custom-table">
            <thead>
            <tr>
                <th style="width: 40%;">Nombre de Categoría</th>
                <th style="width: 20%; text-align: center;">Estado</th>
                <th style="width: 20%; text-align: center;">Productos</th>
                <th style="width: 20%; text-align: center;">Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty listaCategorias}">
                    <c:forEach var="cat" items="${listaCategorias}">
                        <tr>
                            <td>
                                <i class="bi bi-grid-fill me-2"></i> ${cat.nombreCategoria}
                            </td>
                            <!-- Badge de Estado -->
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${cat.estado}">
                                        <span class="badge bg-success">Activa</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">Inactiva</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center fw-bold">
                                    ${cat.totalProductos}
                            </td>
                            <td class="text-center">
                                <!-- Botón Editar (Modal Editar) -->
                                <button type="button" class="btn-edit-trigger border-0 bg-transparent p-0 me-2" title="Editar categoría" onclick="prepararModalEditar('${cat.idCategoria}', '${cat.nombreCategoria}')">
                                    <i class="bi bi-pencil-square action-icon text-dark"></i>
                                </button>

                                <!-- Botón Activar / Desactivar con onclick directo -->
                                <c:choose>
                                    <c:when test="${cat.estado}">
                                        <button type="button" class="btn-delete-trigger border-0 bg-transparent p-0" title="Deshabilitar categoría" onclick="prepararModalEstado('${cat.idCategoria}', '${cat.nombreCategoria}', 'desactivar')">
                                            <i class="bi bi-toggle-on text-success fs-5"></i>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn-delete-trigger border-0 bg-transparent p-0" title="Activar categoría" onclick="prepararModalEstado('${cat.idCategoria}', '${cat.nombreCategoria}', 'activar')">
                                            <i class="bi bi-toggle-off text-secondary fs-5"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="4" class="text-center py-4">No hay categorías registradas.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>

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
<%@ include file="layout/footer.jsp" %>