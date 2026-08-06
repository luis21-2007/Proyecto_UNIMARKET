<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="layout/header_admin.jsp" %>

<div class="container py-4">
    <h1 class="gestion-title">Gestion de Usuarios</h1>
    <!-- Mensajes de Alerta / Feedback -->
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
            <c:choose>
                <c:when test="${param.msg == 'desactivado'}">Usuario desactivado exitosamente.</c:when>
                <c:when test="${param.msg == 'activado'}">Usuario activado exitosamente.</c:when>
            </c:choose>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
            Ocurrió un error al procesar la solicitud del usuario.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <div class="search-input-group d-flex align-items-center w-100 mb-3">
        <i class="bi bi-search text-secondary me-2 fs-5"></i>
        <input type="text" id="filtroUsuario" class="w-100" placeholder="Filtro de Usuarios">
    </div>

    <div class="users-container-card">

        <c:choose>
            <c:when test="${not empty listaUsuarios}">
                <c:forEach var="user" items="${listaUsuarios}">
                    <!-- Item de Usuario desde la Base de Datos -->
                    <div class="user-item-card d-flex align-items-center justify-content-between mb-2">

                        <div class="d-flex align-items-center gap-3">
                            <!-- Avatar con Iniciales -->
                            <div class="user-avatar">
                                    ${fn:substring(user.nombre, 0, 1)}${fn:substring(user.apellido, 0, 1)}
                            </div>

                            <div>
                                <h6 class="mb-0 fw-bold text-dark">${user.nombre} ${user.apellido}</h6>
                                <small class="text-muted fw-semibold">${user.correo}</small>
                            </div>
                        </div>
                        <div class="d-flex align-items-center gap-3">
                            <!-- Badge de Estado -->
                            <c:choose>
                                <c:when test="${user.activo == 1 || user.activo == null}">
                                    <span class="status-badge-active">Activo</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge-inactive">Inactivo</span>
                                </c:otherwise>
                            </c:choose>

                            <!-- Menú Opciones (Tres Puntos) -->
                            <div class="dropdown">
                                <button class="btn-options" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="bi bi-three-dots"></i>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end shadow">
                                    <c:choose>
                                        <%-- Si está Activo: Muestra la opción de Dar de baja --%>
                                        <c:when test="${user.activo == 1 || user.activo == null}">
                                            <li>
                                                <a class="dropdown-item text-danger"
                                                   href="#"
                                                   onclick="prepararModalEstado('${user.id}', '${user.nombre} ${user.apellido}', 'desactivar')">
                                                    <i class="bi bi-slash-circle me-2"></i>Deshabilitar
                                                </a>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li>
                                                <a class="dropdown-item text-success"
                                                   href="#"
                                                   onclick="prepararModalEstado('${user.id}', '${user.nombre} ${user.apellido}', 'activar')">
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
                    <i class="bi bi-people fs-1 text-muted"></i>
                    <p class="text-muted fw-semibold mb-0 mt-2">No se encontraron usuarios registrados en la base de datos.</p>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</div>

<!-- Modal de Confirmación Adaptativo (Activar/Desactivar) -->
<div class="modal fade" id="confirmModalUsuario" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content custom-modal-content">
            <div class="modal-body p-4 text-center">
                <p class="modal-text mb-0">
                    Estás por <span id="accionTextoConfirm"></span> al usuario <strong id="usuarioNombreConfirm"></strong>.<br>¿Estás seguro?
                </p>
                <div class="d-flex justify-content-between mt-4 px-2">
                    <button type="button" class="btn btn-cancelar" data-bs-dismiss="modal">Cancelar</button>
                    <a href="#" id="btnConfirmActionUsuario" class="btn btn-confirmar">Confirmar</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="assets/js/gestion-usuarios.js"></script>
<%@ include file="layout/footer.jsp" %>