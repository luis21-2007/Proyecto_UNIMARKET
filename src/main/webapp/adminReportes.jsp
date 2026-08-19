<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="layout/header_admin.jsp" %>

<div class="container py-4">
    <h2 class="fw-bold mb-1">Gestión de Reportes</h2>
    <p class="text-muted small mb-4">Revisa y gestiona las denuncias enviadas por los usuarios.</p>

    <c:if test="${param.msg == 'estadoActualizado'}">
        <div class="alert alert-success alert-dismissible fade show rounded-3" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> Estado del reporte actualizado con éxito.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty listaReportes}">
            <!-- FORZAMOS DIRECCIÓN VERTICAL Y ANCHO COMPLETO -->
            <div class="d-flex flex-column gap-3 w-100" style="flex-direction: column !important;">
                <c:forEach var="reporte" items="${listaReportes}">

                    <!-- TARJETA DE REPORTE ESTILO ADMIN -->
                    <div class="card border-0 shadow-sm p-3 bg-white w-100" style="border-radius: 18px;">
                        <div class="d-flex flex-column flex-md-row align-items-md-center justify-content-between gap-3 w-100">

                            <!-- INFORMACIÓN DEL USUARIO REPORTADO Y REPORTE -->
                            <div class="d-flex align-items-start gap-3" style="flex: 1;">
                                <!-- Avatar con Iniciales del Reportado -->
                                <div class="rounded-circle d-flex align-items-center justify-content-center text-danger fw-bold flex-shrink-0"
                                     style="width: 48px; height: 48px; background-color: #fce8e6; font-size: 1.1rem;">
                                    <i class="bi bi-exclamation-triangle-fill"></i>
                                </div>

                                <div class="w-100">
                                    <h6 class="fw-bold text-dark mb-0">
                                        Reportado: <c:out value="${reporte.nombreReportado}"/>
                                    </h6>
                                    <div class="text-muted small mb-2">
                                        <c:out value="${reporte.correoReportado}"/>
                                    </div>

                                    <div class="p-2 rounded-3 bg-light border-start border-3 border-danger mb-2">
                                        <span class="badge bg-danger text-white mb-1"><c:out value="${reporte.motivo}"/></span>
                                        <p class="mb-0 text-secondary small text-break">
                                            "<c:out value="${reporte.descripcion}"/>"
                                        </p>
                                    </div>

                                    <div class="text-muted d-flex flex-wrap gap-3" style="font-size: 0.8rem;">
                                        <span><i class="bi bi-person me-1"></i>Denunciante: <strong><c:out value="${reporte.nombreReportador}"/></strong></span>
                                        <c:if test="${not empty reporte.idTransaccion}">
                                            <span><i class="bi bi-receipt me-1"></i> Transacción #${reporte.idTransaccion}</span>
                                        </c:if>
                                        <span><i class="bi bi-clock me-1"></i> <fmt:formatDate value="${reporte.fechaReporte}" pattern="dd/MM/yyyy HH:mm"/></span>
                                    </div>
                                </div>
                            </div>

                            <!-- ESTADO (PILL BADGE) Y ACCIONES -->
                            <div class="d-flex align-items-center justify-content-between justify-content-md-end gap-3 flex-shrink-0">

                                    <%-- Insignias dinámicas de Estado --%>
                                <c:choose>
                                    <c:when test="${reporte.estado == 0}">
                                        <span class="badge rounded-pill bg-warning text-dark px-3 py-2 fw-semibold border border-warning">Pendiente</span>
                                    </c:when>
                                    <c:when test="${reporte.estado == 1}">
                                        <span class="badge rounded-pill bg-info text-dark px-3 py-2 fw-semibold border border-info">En Revisión</span>
                                    </c:when>
                                    <c:when test="${reporte.estado == 2}">
                                        <span class="badge rounded-pill bg-danger text-white px-3 py-2 fw-semibold">Sancionado</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge rounded-pill bg-secondary text-white px-3 py-2 fw-semibold">Desestimado</span>
                                    </c:otherwise>
                                </c:choose>

                                <!-- MENÚ DESPLEGABLE DE TRES PUNTOS -->
                                <div class="dropdown">
                                    <button class="btn btn-light btn-sm rounded-circle p-2 border-0 shadow-none"
                                            type="button" data-bs-toggle="dropdown" aria-expanded="false"
                                            style="width: 38px; height: 38px;">
                                        <i class="bi bi-three-dots fs-5"></i>
                                    </button>
                                    <ul class="dropdown-menu dropdown-menu-end shadow border-0 rounded-3">
                                        <li><h6 class="dropdown-header">Cambiar Estado</h6></li>

                                        <c:if test="${reporte.estado != 1}">
                                            <li>
                                                <form action="adminReportes" method="POST">
                                                    <input type="hidden" name="idReporte" value="${reporte.idReporte}">
                                                    <input type="hidden" name="nuevoEstado" value="1">
                                                    <button class="dropdown-item d-flex align-items-center gap-2 py-2" type="submit">
                                                        <i class="bi bi-search text-info"></i> Marcar En Revisión
                                                    </button>
                                                </form>
                                            </li>
                                        </c:if>

                                        <c:if test="${reporte.estado != 2}">
                                            <li>
                                                <form action="adminReportes" method="POST">
                                                    <input type="hidden" name="idReporte" value="${reporte.idReporte}">
                                                    <input type="hidden" name="nuevoEstado" value="2">
                                                    <button class="dropdown-item d-flex align-items-center gap-2 text-danger py-2" type="submit">
                                                        <i class="bi bi-slash-circle text-danger"></i> Aplicar Sanción
                                                    </button>
                                                </form>
                                            </li>
                                        </c:if>

                                        <c:if test="${reporte.estado != 3}">
                                            <li>
                                                <form action="adminReportes" method="POST">
                                                    <input type="hidden" name="idReporte" value="${reporte.idReporte}">
                                                    <input type="hidden" name="nuevoEstado" value="3">
                                                    <button class="dropdown-item d-flex align-items-center gap-2 text-muted py-2" type="submit">
                                                        <i class="bi bi-x-circle text-secondary"></i> Desestimar Reporte
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
            </div>
        </c:when>
        <c:otherwise>
            <div class="text-center py-5 bg-white rounded-4 shadow-sm p-4">
                <i class="bi bi-shield-check fs-1 text-success"></i>
                <h5 class="fw-bold text-dark mt-3">Sin reportes registrados</h5>
                <p class="text-muted small mb-0">No hay ninguna denuncia o reporte de usuario por el momento.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="layout/footer.jsp" %>