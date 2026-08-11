<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />

<%@ include file="layout/header.jsp" %>

<link href="assets/css/perfil-estilos.css" rel="stylesheet">

<div class="container-fluid py-4 profile-main-container">
    <div class="container">
        <!-- MENSAJES DE ÉXITO Y ERROR -->
        <c:if test="${param.error == 'yaCalificado'}">
            <div class="alert alert-info alert-dismissible fade show my-3" role="alert">
                <i class="bi bi-info-circle-fill me-2"></i> Esta compra ya ha sido calificada anteriormente.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${param.msg == 'calificacionExitosa'}">
            <div class="alert alert-success alert-dismissible fade show my-3" role="alert">
                <i class="bi bi-star-fill me-2"></i> ¡Gracias! Tu calificación se ha registrado correctamente.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <c:if test="${param.msg == 'reporteExitoso'}">
            <div class="alert alert-warning alert-dismissible fade show my-3" role="alert">
                <i class="bi bi-shield-check me-2"></i> Tu reporte ha sido enviado. Un administrador revisará el caso.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <c:if test="${param.error == 'camposVacios' || param.error == 'datosInvalidos' || param.error == 'errorReporte'}">
            <div class="alert alert-danger alert-dismissible fade show my-3" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i> No se pudo procesar tu reporte. Por favor, completa todos los campos requeridos.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <div class="row g-4">

            <!-- PANEL LATERAL DE MENÚ -->
            <div class="col-md-4 col-lg-3">
                <div class="profile-sidebar-card shadow-sm">
                    <div class="profile-sidebar-title ms-2">Perfil</div>

                    <nav class="d-flex flex-column">
                        <a href="perfil" class="profile-menu-item">
                            <i class="bi bi-person"></i> Ajustes
                        </a>
                        <a href="misVentas" class="profile-menu-item">
                            <i class="bi bi-bag-check"></i> Mis ventas
                        </a>
                        <a href="misCompras" class="profile-menu-item active">
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

            <!-- CONTENIDO PRINCIPAL: MIS COMPRAS -->
            <div class="col-md-8 col-lg-9 ps-md-4">
                <h1 class="profile-content-title mb-1">Mis Compras</h1>
                <p class="text-muted fw-semibold small mb-4">Historial de productos que has adquirido en la plataforma.</p>

                <div class="compras-container">
                    <c:choose>
                        <c:when test="${not empty listaCompras}">
                            <c:forEach var="compra" items="${listaCompras}">
                                <div class="card border-0 shadow-sm rounded-3 mb-3 p-3 bg-white">
                                    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">

                                        <!-- Información de la Compra -->
                                        <div>
                                            <h5 class="fw-bold text-dark mb-1"><c:out value="${compra.nombreProducto}"/></h5>
                                            <p class="mb-1 text-muted fw-semibold">
                                                Monto pagado:
                                                <span class="text-success fw-bold">
                                                    <fmt:formatNumber value="${compra.monto}" type="currency" currencySymbol="$"/>
                                                </span>
                                            </p>
                                            <small class="text-secondary d-block">
                                                <i class="bi bi-shop me-1"></i>Vendedor: <strong><c:out value="${compra.nombreVendedor}"/></strong>
                                                <c:if test="${not empty compra.telefonoVendedor}">
                                                    | Tel: <c:out value="${compra.telefonoVendedor}"/>
                                                </c:if>
                                                | Fecha: <fmt:formatDate value="${compra.fechaTransaccion}" pattern="dd/MM/yyyy HH:mm"/>
                                            </small>
                                        </div>

                                        <!-- Estado + Menú Desplegable (Tres Puntos) -->
                                        <div class="d-flex align-items-center justify-content-end gap-2">

                                            <!-- BADGE DINÁMICO DE ESTADO DE COMPRA -->
                                            <div class="text-md-end">
                                                <c:choose>
                                                    <c:when test="${compra.estado == 1}">
                                                        <span class="badge bg-success px-3 py-2 rounded-pill fs-6 d-inline-block">
                                                            <i class="bi bi-check-circle-fill me-1"></i> Compra Completada
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${compra.estado == 2}">
                                                        <span class="badge bg-warning text-dark px-3 py-2 rounded-pill fs-6 d-inline-block">
                                                            <i class="bi bi-clock-history me-1"></i> En Proceso
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-danger px-3 py-2 rounded-pill fs-6 d-inline-block">
                                                            <i class="bi bi-x-circle-fill me-1"></i> Cancelada
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>

                                            <!-- MENÚ DE TRES PUNTOS -->
                                            <div class="dropdown">
                                                <button class="btn btn-light btn-sm rounded-circle p-2 border-0 shadow-none"
                                                        type="button"
                                                        data-bs-toggle="dropdown"
                                                        aria-expanded="false"
                                                        style="width: 38px; height: 38px;">
                                                    <i class="bi bi-three-dots-vertical fs-6"></i>
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 rounded-3">

                                                    <!-- Solo si la compra está completada (estado == 1) -->
                                                    <c:if test="${compra.estado == 1}">
                                                        <c:choose>
                                                            <%-- Caso A: YA FUE CALIFICADO --%>
                                                            <c:when test="${compra.yaCalificado}">
                                                                <li>
                                                                    <button class="dropdown-item d-flex align-items-center gap-2 text-muted py-2 disabled"
                                                                            type="button" disabled>
                                                                        <i class="bi bi-check-circle-fill text-success"></i> Vendedor Calificado
                                                                    </button>
                                                                </li>
                                                            </c:when>

                                                            <%-- Caso B: AÚN NO HA SIDO CALIFICADO --%>
                                                            <c:otherwise>
                                                                <li>
                                                                    <button class="dropdown-item d-flex align-items-center gap-2 text-dark py-2"
                                                                            type="button"
                                                                            data-bs-toggle="modal"
                                                                            data-bs-target="#modalCalificar"
                                                                            onclick="prepararCalificacion('${compra.idVendedor}', '${compra.nombreVendedor}', '${compra.idTransaccion}')">
                                                                        <i class="bi bi-star-fill text-warning"></i> Calificar Vendedor
                                                                    </button>
                                                                </li>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <li><hr class="dropdown-divider my-1"></li>
                                                    </c:if>

                                                    <!-- Opción de Reportar Vendedor (Pasa también idTransaccion) -->
                                                    <li>
                                                        <button class="dropdown-item d-flex align-items-center gap-2 text-danger py-2"
                                                                type="button"
                                                                data-bs-toggle="modal"
                                                                data-bs-target="#modalReportar"
                                                                onclick="prepararReporte('${compra.idVendedor}', '${compra.nombreVendedor}', '${compra.idTransaccion}')">
                                                            <i class="bi bi-exclamation-triangle-fill"></i> Reportar Vendedor
                                                        </button>
                                                    </li>
                                                </ul>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-5 bg-white rounded-3 shadow-sm p-4">
                                <i class="bi bi-cart-x fs-1 text-muted"></i>
                                <h5 class="fw-bold text-dark mt-3">Sin compras realizadas</h5>
                                <p class="text-muted small mb-0">Cuando adquieras un producto aceptando ofertas, aparecerá en esta sección.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div>
    </div>
</div>

<!-- MODAL CALIFICAR -->
<div class="modal fade" id="modalCalificar" tabindex="-1" aria-labelledby="modalCalificarLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 rounded-4 shadow">
            <div class="modal-header border-0 pb-0">
                <h5 class="modal-title fw-bold text-dark" id="modalCalificarLabel">
                    <i class="bi bi-star-fill text-warning me-2"></i>Calificar Vendedor
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="guardarCalificacion" method="POST">
                <div class="modal-body">
                    <input type="hidden" name="idVendedor" id="calif_idVendedor">
                    <input type="hidden" name="idTransaccion" id="calif_idTransaccion">

                    <p class="text-secondary small mb-3">
                        ¿Cómo fue tu experiencia comprando a <strong id="calif_nombreVendedor" class="text-dark"></strong>?
                    </p>

                    <!-- Puntuación -->
                    <div class="mb-3 text-center">
                        <label class="form-label d-block fw-semibold text-muted mb-2">Puntuación</label>
                        <div class="d-flex justify-content-center gap-2 fs-3 text-warning">
                            <select name="puntuacion" class="form-select text-center fw-bold" required style="max-width: 180px;">
                                <option value="5" selected>⭐⭐⭐⭐⭐ (5/5)</option>
                                <option value="4">⭐⭐⭐⭐ (4/5)</option>
                                <option value="3">⭐⭐⭐ (3/5)</option>
                                <option value="2">⭐⭐ (2/5)</option>
                                <option value="1">⭐ (1/5)</option>
                            </select>
                        </div>
                    </div>

                    <div class="mb-2">
                        <label for="comentarioCalificacion" class="form-label fw-semibold text-secondary small">Comentario (Opcional)</label>
                        <textarea class="form-control" id="comentarioCalificacion" name="comentario" rows="3" placeholder="Escribe un breve comentario sobre el vendedor..."></textarea>
                    </div>
                </div>
                <div class="modal-footer border-0 pt-0">
                    <button type="button" class="btn btn-light rounded-pill px-3 btn-sm" data-bs-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-warning text-dark fw-bold rounded-pill px-4 btn-sm">Enviar Calificación</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- MODAL REPORTAR -->
<div class="modal fade" id="modalReportar" tabindex="-1" aria-labelledby="modalReportarLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 rounded-4 shadow">
            <div class="modal-header border-0 pb-0">
                <h5 class="modal-title fw-bold text-danger" id="modalReportarLabel">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>Reportar Vendedor
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="guardarReporte" method="POST">
                <div class="modal-body">
                    <!-- INPUTS OCULTOS DE REPORTADO E ID TRANSACCIÓN -->
                    <input type="hidden" name="idReportado" id="rep_idVendedor">
                    <input type="hidden" name="idTransaccion" id="rep_idTransaccion">

                    <p class="text-secondary small mb-3">
                        Estás a punto de enviar una denuncia sobre <strong id="rep_nombreVendedor" class="text-dark"></strong> a los administradores.
                    </p>

                    <!-- Motivo -->
                    <div class="mb-3">
                        <label for="motivoReporte" class="form-label fw-semibold text-secondary small">Motivo de la denuncia</label>
                        <select class="form-select" id="motivoReporte" name="motivo" required>
                            <option value="" disabled selected>Selecciona una opción...</option>
                            <option value="Intento de Fraude">Intento de Fraude o Estafa</option>
                            <option value="Producto no entregado">Producto no entregado / Falso</option>
                            <option value="Comportamiento Inapropiado">Lenguaje o Comportamiento Inapropiado</option>
                            <option value="Spam / Datos Falsos">Spam o Datos de contacto falsos</option>
                            <option value="Otro">Otro motivo</option>
                        </select>
                    </div>

                    <!-- Detalles -->
                    <div class="mb-2">
                        <label for="descripcionReporte" class="form-label fw-semibold text-secondary small">Detalles del reporte</label>
                        <textarea class="form-control" id="descripcionReporte" name="descripcion" rows="3" placeholder="Explica lo sucedido con el mayor detalle posible..." required></textarea>
                    </div>
                </div>
                <div class="modal-footer border-0 pt-0">
                    <button type="button" class="btn btn-light rounded-pill px-3 btn-sm" data-bs-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-danger fw-bold rounded-pill px-4 btn-sm">Enviar Reporte</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="assets/js/Compras.js"></script>

<%@ include file="layout/footer.jsp" %>