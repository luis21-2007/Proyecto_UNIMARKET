<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header_admin.jsp" %>

<div class="container mt-4 mb-5">

    <!-- Mensajes de Alerta/Feedback -->
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
            <c:choose>
                <c:when test="${param.msg == 'creada'}">Categoría creada exitosamente.</c:when>
                <c:when test="${param.msg == 'actualizada'}">Categoría actualizada exitosamente.</c:when>
                <c:when test="${param.msg == 'eliminada'}">Categoría eliminada exitosamente.</c:when>
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
                <th style="width: 50%;">Nombre de Categoría</th>
                <th style="width: 25%; text-align: center;">Productos</th>
                <th style="width: 25%; text-align: center;">Acciones</th>
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
                            <td class="text-center">0</td>
                            <td class="text-center">
                                <!-- Botón Editar (Abre el Modal) -->
                                <button type="button"
                                        class="btn-edit-trigger border-0 bg-transparent p-0"
                                        title="Editar categoría"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editCategoriaModal"
                                        data-id="${cat.idCategoria}"
                                        data-nombre="${cat.nombreCategoria}">
                                    <i class="bi bi-pencil-square action-icon text-dark me-2"></i>
                                </button>

                                <!-- Botón Deshabilitar/Eliminar (Abre el Modal de Confirmación) -->
                                <button type="button"
                                        class="btn-delete-trigger border-0 bg-transparent p-0"
                                        title="Eliminar categoría"
                                        data-bs-toggle="modal"
                                        data-bs-target="#confirmModal"
                                        data-id="${cat.idCategoria}"
                                        data-nombre="${cat.nombreCategoria}">
                                    <i class="bi bi-toggle-on text-green toggle-btn"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="3" class="text-center py-4">No hay categorías registradas.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>

        <!-- Paginación -->
        <div class="table-footer">
            <div>Página 1 de 1</div>
            <div class="pagination-controls">
                <span><i class="bi bi-chevron-left"></i></span>
                <span class="active-page">1</span>
                <span><i class="bi bi-chevron-right"></i></span>
            </div>
        </div>
    </div>

</div>

<!-- Modal de Confirmación de Eliminación -->
<div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content custom-modal-content">
            <div class="modal-body p-4 text-center">
                <p class="modal-text mb-0" id="modalDynamicText">
                    Estás por deshabilitar la categoría <strong id="categoriaNombreConfirm"></strong>.<br>¿Estás seguro?
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
                    <!-- Acción y ID para el Servlet -->
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" id="editCategoriaId" name="categoriaId">

                    <div class="mb-3 text-start">
                        <label for="editNombreCategoria" class="edit-field-label">Nombre Categoría</label>
                        <div class="edit-input-group">
                            <input type="text" id="editNombreCategoria" name="nombre" class="edit-input w-100" required>
                        </div>
                    </div>

                    <div class="mb-4 text-start">
                        <label class="edit-field-label">Ícono Categoría</label>
                        <div class="icon-upload-group d-flex align-items-center gap-2">
                            <label for="editIconoCategoria" class="icon-upload-btn btn btn-outline-secondary" title="Subir nuevo ícono">
                                <i class="bi bi-upload"></i>
                            </label>
                            <input type="file" id="editIconoCategoria" name="icono" accept="image/*" hidden>
                            <span class="icon-upload-filename" id="editIconoNombre">Sin archivo</span>
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

<script src="assets/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/gestion_categorias.js"></script>
<%@ include file="layout/footer.jsp" %>