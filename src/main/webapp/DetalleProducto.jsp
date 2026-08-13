<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- FORZA EL USO DE PUNTO (.) PARA DECIMALES EN LUGAR DE COMA (,) --%>
<fmt:setLocale value="en_US" />

<%@ include file="layout/header.jsp" %>

<!-- ALERTA DE ÉXITO -->
<c:if test="${param.msg == 'ofertaExitosa'}">
    <div class="alert alert-success alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-check-circle-fill me-2"></i> Tu oferta ha sido enviada al vendedor exitosamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<!-- ALERTA DE ERROR: AUTO-OFERTA NO PERMITIDA -->
<c:if test="${param.error == 'auto_oferta'}">
    <div class="alert alert-danger alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-exclamation-triangle-fill me-2"></i> No puedes realizar una oferta en tu propio producto.
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
            <div class="d-flex align-items-center mb-2 text-secondary fs-6">
                <i class="bi bi-person-circle text-warning fs-5 me-2"></i>
                <span>Vendido por:
                    <strong class="text-dark">
                        ${not empty vendedor ? vendedor.nombre : (not empty producto.nombreUsuario ? producto.nombreUsuario : 'Usuario UTEZ')}
                    </strong>
                </span>
            </div>

            <!-- APARTADO NUEVO: CALIFICACIÓN DEL VENDEDOR -->
            <div class="d-flex align-items-center mb-3">
                <c:choose>
                    <c:when test="${not empty promedioVendedor and promedioVendedor > 0}">
                        <div class="d-flex align-items-center bg-light px-3 py-1 rounded-pill border">
                            <!-- Estrellas Dinámicas -->
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

                            <!-- Valor numérico del Promedio y Cantidad de Reseñas -->
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

            <!-- BOTÓN OFERTAR / VALIDACIONES -->
            <div>
                <c:choose>
                    <%-- CASO 1: El usuario en sesión es el dueño del producto --%>
                    <c:when test="${not empty sessionScope.usuario and sessionScope.usuario.id == producto.idUsuario}">
                        <button type="button" class="btn btn-secondary w-100 text-center fw-bold py-2" disabled style="cursor: not-allowed; opacity: 0.7;">
                            <i class="bi bi-person-check-fill me-1"></i> ES TU PUBLICACIÓN
                        </button>
                    </c:when>

                    <%-- CASO 2: La oferta fue ACEPTADA por el vendedor (estado = 1) --%>
                    <c:when test="${estadoOferta == 1}">
                        <button type="button" class="btn btn-success text-white w-100 text-center fw-bold py-2" disabled style="cursor: default; opacity: 0.9;">
                            <i class="bi bi-check-circle-fill me-1"></i> ¡OFERTA ACEPTADA!
                        </button>
                    </c:when>

                    <%-- CASO 3: El usuario envió oferta y está PENDIENTE (estado = 0) --%>
                    <c:when test="${estadoOferta == 0}">
                        <button type="button" class="btn btn-warning text-dark w-100 text-center fw-bold py-2" disabled style="cursor: not-allowed; opacity: 0.85;">
                            <i class="bi bi-clock-history me-1"></i> OFERTA ENVIADA
                        </button>
                    </c:when>

                    <%-- CASO 4: Comprador no ha ofertado o le rechazaron la anterior (estado = -1 o 2) --%>
                    <c:otherwise>
                        <button type="button" class="btn btn-comprar-detalle w-100 text-center fw-bold py-2" data-bs-toggle="modal" data-bs-target="#modalOferta">
                            OFERTAR
                        </button>
                    </c:otherwise>
                </c:choose>
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

                <!-- FORMULARIO DE OFERTA -->
                <form id="formEnviarOferta" action="enviarOferta" method="POST">
                    <div class="modal-body">
                        <!-- ID Oculto del Producto -->
                        <input type="hidden" name="idProducto" value="${producto.idProducto}">

                        <div class="mb-3">
                            <label for="montoOferta" class="form-label fw-medium text-secondary">¿Cuánto deseas ofrecer por este producto?</label>

                            <!-- Input numérico con símbolo de pesos ($) -->
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
</div>
<script src="assets/js/detalles-productos.js"></script>
<%@ include file="layout/footer.jsp" %>