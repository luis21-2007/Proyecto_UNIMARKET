<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />

<%@ include file="layout/header.jsp" %>

<!-- ALERTAS DE SISTEMA Y REPORTES -->
<c:if test="${param.msg == 'reporteExitoso'}">
    <div class="alert alert-info alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-shield-check me-2"></i> Tu reporte ha sido enviado. Los administradores revisarán la publicación a la brevedad.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<c:if test="${param.error == 'autoReporte'}">
    <div class="alert alert-warning alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-exclamation-triangle-fill me-2"></i> No puedes reportar tu propio producto o perfil.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<c:if test="${param.error == 'ya_vendido' or param.error == 'compra_fallida'}">
    <div class="alert alert-danger alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-x-circle-fill me-2"></i> Lo sentimos, este producto acaba de ser adquirido o reservado por otro usuario.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<c:if test="${transaccionCancelada}">
    <div class="alert alert-warning alert-dismissible fade show rounded-3 shadow-sm mb-3" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
        Tu solicitud de compra anterior fue cancelada o rechazada por el vendedor. El producto vuelve a estar disponible.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<!-- ALERTA DE ÉXITO: OFERTA -->
<c:if test="${param.msg == 'ofertaExitosa'}">
    <div class="alert alert-success alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-check-circle-fill me-2"></i> Tu oferta ha sido enviada al vendedor exitosamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<!-- ALERTA DE ÉXITO: COMPRA -->
<c:if test="${param.msg == 'compraExitosa'}">
    <div class="alert alert-success alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-bag-check-fill me-2"></i> ¡Felicidades! Has realizado la compra exitosamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<!-- ALERTA DE ERROR: AUTO-OFERTA / AUTO-COMPRA NO PERMITIDA -->
<c:if test="${param.error == 'auto_oferta'}">
    <div class="alert alert-danger alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-exclamation-triangle-fill me-2"></i> No puedes realizar acciones en tu propio producto.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<!-- ALERTA DE ERROR: OFERTA YA EXISTENTE -->
<c:if test="${param.error == 'oferta_existente'}">
    <div class="alert alert-warning alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-exclamation-circle-fill me-2"></i> Ya tienes una oferta pendiente para este producto. Espera a que el vendedor la responda.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<div class="container py-5">
    <div class="row g-4 align-items-center">
        <!-- COLUMNA 1: MINIATURAS DE IMÁGENES -->
        <div class="col-12 col-md-3 col-lg-2">
            <div class="d-flex flex-row flex-md-column gap-3 justify-content-center">
                <c:choose>
                    <c:when test="${not empty listaImagenes}">
                        <c:forEach var="img" items="${listaImagenes}" varStatus="status">
                            <img src="${img}" class="thumb-img ${status.first ? 'active-thumb' : ''}"
                                 onclick="cambiarImagenPrincipal(this.src, this)" alt="Miniatura">
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <img src="assets/img/icono-integradora.jpeg" class="thumb-img active-thumb" alt="Default">
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- COLUMNA 2: IMAGEN PRINCIPAL -->
        <div class="col-12 col-md-5 col-lg-5">
            <img id="imgPrincipal"
                 src="${not empty listaImagenes ? listaImagenes[0] : 'assets/img/icono-integradora.jpeg'}"
                 class="main-detail-img" alt="${producto.nombre}">
        </div>

        <!-- COLUMNA 3: DETALLES DEL PRODUCTO -->
        <div class="col-12 col-md-4 col-lg-5 ps-lg-4">
            <!-- Título del Producto -->
            <h1 class="product-detail-title mb-2">${producto.nombre}</h1>

            <!-- VENDEDOR DEL PRODUCTO -->
            <div class="d-flex align-items-center flex-wrap gap-2 mb-2 text-secondary fs-6">
                <i class="bi bi-person-circle text-warning fs-5"></i>
                <span>Vendido por:
                    <strong class="text-dark">
                        ${not empty vendedor ? vendedor.nombre : (not empty producto.nombreUsuario ? producto.nombreUsuario : 'Usuario UTEZ')}
                    </strong>
                </span>

                <!-- MUESTRA LA CANTIDAD DE REPORTES SANCIONADOS DEL VENDEDOR -->
                <span class="badge bg-danger rounded-pill px-2 py-1" style="font-size: 0.75rem;" title="Cantidad de reportes procedentes/sancionados recibidos">
                    <i class="bi bi-shield-exclamation me-1"></i>reportes = ${not empty cantidadReportesSancionados ? cantidadReportesSancionados : 0}
                </span>
            </div>

            <!-- APARTADO: CALIFICACIÓN DEL VENDEDOR -->
            <div class="d-flex align-items-center mb-3">
                <c:choose>
                    <c:when test="${not empty promedioVendedor and promedioVendedor > 0}">
                        <div class="d-flex align-items-center bg-light px-3 py-1 rounded-pill border">
                            <div class="text-warning fs-6 me-2">
                                <c:forEach var="i" begin="1" end="5">
                                    <c:choose>
                                        <c:when test="${i <= promedioVendedor}">
                                            <i class="bi bi-star-fill"></i>
                                        </c:when>
                                        <c:when test="${i - 0.5 <= promedioVendedor}">
                                            <i class="bi bi-star-half"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="bi bi-star"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>

                            <span class="fw-bold text-dark me-1" style="font-size: 0.9rem;">
                                <fmt:formatNumber value="${promedioVendedor}" maxFractionDigits="1" minFractionDigits="1" />
                            </span>
                            <span class="text-muted small">
                                (${totalResenasVendedor} ${totalResenasVendedor == 1 ? 'reseña' : 'reseñas'})
                            </span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="d-flex align-items-center bg-light px-3 py-1 rounded-pill border">
                            <i class="bi bi-star text-muted me-1"></i>
                            <span class="text-muted small fw-semibold">Nuevo vendedor (Sin reseñas)</span>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- BOTÓN / ESTADO DE REPORTE DEL VENDEDOR -->
            <c:if test="${not esDuenoProducto}">
                <div class="mb-3 text-end">
                    <c:choose>
                        <c:when test="${yaReportoVendedor}">
                            <span class="text-muted small fw-semibold">
                                <i class="bi bi-check-circle-fill text-success me-1"></i> Vendedor reportado
                            </span>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn btn-link text-danger text-decoration-none p-0 small fw-semibold" data-bs-toggle="modal" data-bs-target="#modalReportar">
                                <i class="bi bi-flag-fill me-1"></i> Reportar vendedor por producto
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <!-- TARJETA DE DESCRIPCIÓN -->
            <div class="description-card mb-4">
                <div class="d-flex align-items-center text-muted small mb-2">
                    <i class="bi bi-clock-history me-1"></i>
                    <span>Publicado el
                        <c:choose>
                            <c:when test="${not empty producto.fechaPublicacion}">
                                <fmt:formatDate value="${producto.fechaPublicacion}" pattern="dd/MM/yyyy 'a las' hh:mm a" />
                            </c:when>
                            <c:otherwise>
                                Recientemente
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <span class="description-title text-dark">Descripción</span>
                <p class="mb-0 text-secondary" style="white-space: pre-line;">${producto.descripcion}</p>
            </div>

            <!-- PRECIO -->
            <div class="mb-4">
                <h2 class="fw-bold" style="color: #e67e22;">
                    <fmt:formatNumber currencySymbol="$" value="${producto.precio}" type="currency"/>
                </h2>
            </div>

            <!-- BOTONES DE ACCIÓN -->
            <div>
                <c:choose>
                    <c:when test="${not empty sessionScope.usuario and sessionScope.usuario.id == producto.idUsuario}">
                        <button type="button" class="btn btn-secondary w-100 text-center fw-bold py-2" disabled style="border-radius: 25px;">
                            <i class="bi bi-person-check-fill me-1"></i> ES TU PUBLICACIÓN
                        </button>
                    </c:when>

                    <c:when test="${yaComproDirecto or estadoOferta == 1}">
                        <div class="d-flex flex-column gap-2">
                            <span class="badge bg-success px-3 py-2 rounded-pill fs-6 text-center">
                                <i class="bi bi-check-circle-fill me-1"></i>
                                <c:choose>
                                    <c:when test="${estadoOferta == 1}">Oferta Aceptada</c:when>
                                    <c:otherwise>Compra Registrada (En Proceso)</c:otherwise>
                                </c:choose>
                            </span>

                            <a href="https://wa.me/${vendedor.telefono}?text=Hola%20${vendedor.nombre},%20me%20interesa%20coordinar%20la%20entrega%20de%20mi%20compra/oferta%20por%20${producto.nombre}."
                               target="_blank"
                               class="btn btn-success btn-lg w-100 fw-bold shadow-sm d-inline-flex align-items-center justify-content-center gap-2"
                               style="border-radius: 25px;">
                                <i class="bi bi-whatsapp fs-5"></i> Contactar por WhatsApp
                            </a>
                        </div>
                    </c:when>

                    <c:when test="${producto.estado == 3}">
                        <button type="button" class="btn btn-secondary text-white w-100 text-center fw-bold py-2" disabled style="border-radius: 25px;">
                            <i class="bi bi-clock-history me-1"></i> PRODUCTO RESERVADO / EN PROCESO
                        </button>
                    </c:when>

                    <c:when test="${estadoOferta == 0}">
                        <button type="button" class="btn btn-warning text-dark w-100 text-center fw-bold py-2" disabled style="border-radius: 25px;">
                            <i class="bi bi-clock-history me-1"></i> OFERTA ENVIADA
                        </button>
                    </c:when>

                    <c:otherwise>
                        <div class="d-flex gap-2">
                            <button type="button" class="btn btn-comprar-detalle w-50 text-center fw-bold py-2 shadow-sm" data-bs-toggle="modal" data-bs-target="#modalComprar" style="border-radius: 25px;">
                                <i class="bi bi-bag-check-fill me-1"></i> COMPRAR
                            </button>
                            <button type="button" class="btn btn-comprar-detalle w-50 text-center fw-bold py-2 shadow-sm" data-bs-toggle="modal" data-bs-target="#modalOferta" style="border-radius: 25px;">
                                <i class="bi bi-tag-fill me-1"></i> OFERTAR
                            </button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- MODAL DE COMPRA -->
    <div class="modal fade" id="modalComprar" tabindex="-1" aria-labelledby="modalComprarLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content" style="border-radius: 15px;">

                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold text-dark" id="modalComprarLabel">
                        <i class="bi bi-bag-check-fill me-2" style="color: #8B0000;"></i>Confirmar Compra Directa
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form id="formComprarProducto" action="comprarProducto" method="POST">
                    <div class="modal-body text-center py-4">
                        <input type="hidden" name="idProducto" value="${producto.idProducto}">

                        <i class="bi bi-cart-check display-3 text-muted mb-3 d-block"></i>
                        <h6 class="text-secondary mb-2">Estás a punto de comprar:</h6>
                        <h5 class="fw-bold text-dark mb-3">${producto.nombre}</h5>

                        <div class="p-3 bg-light rounded-3 d-inline-block border">
                            <span class="text-muted me-2">Precio total:</span>
                            <span class="fw-bold fs-4" style="color: #e67e22;">
                                <fmt:formatNumber currencySymbol="$" value="${producto.precio}" type="currency"/>
                            </span>
                        </div>
                    </div>

                    <div class="modal-footer border-0 pt-0 justify-content-end gap-2">
                        <button type="button" class="btn btn-outline-secondary btn-sm px-3 rounded-pill" data-bs-dismiss="modal">
                            Cancelar
                        </button>
                        <button type="submit" class="btn text-white btn-sm px-4 fw-bold rounded-pill" style="background-color: #8B0000; border-color: #8B0000;">
                            Confirmar Compra
                        </button>
                    </div>
                </form>

            </div>
        </div>
    </div>

    <!-- MODAL DE OFERTA -->
    <div class="modal fade" id="modalOferta" tabindex="-1" aria-labelledby="modalOfertaLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content" style="border-radius: 15px;">

                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold text-dark" id="modalOfertaLabel">
                        <i class="bi bi-tag-fill text-warning me-2"></i>Realizar Oferta
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form id="formEnviarOferta" action="enviarOferta" method="POST">
                    <div class="modal-body">
                        <input type="hidden" name="idProducto" value="${producto.idProducto}">

                        <div class="mb-3">
                            <label for="montoOferta" class="form-label fw-medium text-secondary">¿Cuánto deseas ofrecer por este producto?</label>

                            <div class="input-group">
                                <span class="input-group-text bg-light fw-bold text-secondary" style="border-radius: 10px 0 0 10px;">$</span>
                                <input type="number" step="0.01" min="1" max="${producto.precio}" class="form-control form-control-lg fw-bold" id="montoOferta" name="montoOferta" placeholder="0.00" required style="border-radius: 0 10px 10px 0; color: #8B0000;">
                            </div>
                            <div class="form-text mt-1 text-muted small">
                                Precio original: <strong>$<fmt:formatNumber value="${producto.precio}" type="number" minFractionDigits="2" maxFractionDigits="2"/></strong>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer border-0 pt-0 justify-content-end gap-2">
                        <button type="button" class="btn btn-outline-secondary btn-sm px-3 rounded-pill" data-bs-dismiss="modal">
                            Cancelar
                        </button>
                        <button type="submit" id="btnSubmitOferta" class="btn btn-danger btn-sm px-4 fw-bold rounded-pill" style="background-color: #8B0000; border-color: #8B0000;">
                            Enviar oferta
                        </button>
                    </div>
                </form>

            </div>
        </div>
    </div>

    <!-- MODAL DE REPORTE -->
    <div class="modal fade" id="modalReportar" tabindex="-1" aria-labelledby="modalReportarLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content" style="border-radius: 15px;">

                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold text-danger" id="modalReportarLabel">
                        <i class="bi bi-exclamation-octagon-fill me-2"></i>Reportar Publicación / Vendedor
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form id="formReportar" action="guardarReporte" method="POST">
                    <div class="modal-body">
                        <!-- Campos Ocultos para Control y Redirección -->
                        <input type="hidden" name="origen" value="detalleProducto">
                        <input type="hidden" name="idProducto" value="${producto.idProducto}">
                        <input type="hidden" name="idReportado" value="${producto.idUsuario}">
                        <input type="hidden" name="idTransaccion" value="">

                        <div class="mb-3 text-start">
                            <label for="motivo" class="form-label fw-medium text-secondary">Motivo del reporte:</label>
                            <select class="form-select" id="motivo" name="motivo" required style="border-radius: 10px;">
                                <option value="" selected disabled>Selecciona un motivo</option>
                                <option value="Contenido Inapropiado / Ofensivo">Contenido inapropiado u ofensivo</option>
                                <option value="Producto Prohibido">Producto no permitido o prohibido</option>
                                <option value="Posible Estafa / Fraude">Posible estafa o fraude</option>
                                <option value="Informacion Falsa / Enganosa">Información falsa o engañosa</option>
                                <option value="Otro">Otro motivo</option>
                            </select>
                        </div>

                        <div class="mb-3 text-start">
                            <label for="descripcionReporte" class="form-label fw-medium text-secondary">Detalles del reporte:</label>
                            <textarea class="form-control" id="descripcionReporte" name="descripcion" rows="3" placeholder="Describe brevemente el motivo..." required style="border-radius: 10px;"></textarea>
                        </div>
                    </div>

                    <div class="modal-footer border-0 pt-0 justify-content-end gap-2">
                        <button type="button" class="btn btn-outline-secondary btn-sm px-3 rounded-pill" data-bs-dismiss="modal">
                            Cancelar
                        </button>
                        <button type="submit" class="btn btn-danger btn-sm px-4 fw-bold rounded-pill">
                            Enviar Reporte
                        </button>
                    </div>
                </form>

            </div>
        </div>
    </div>

</div>

<script src="assets/js/detalles-productos.js"></script>
<%@ include file="layout/footer.jsp" %>