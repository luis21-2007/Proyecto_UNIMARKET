<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />

<%@ include file="layout/header.jsp" %>
<link href="${pageContext.request.contextPath}/assets/css/estilos-gestionCategorias.css?v=1.0" rel="stylesheet">

<div class="container py-5">

    <!-- ALERTAS DE FEEDBACK -->
    <c:if test="${param.msg == 'actualizacionExitosa'}">
        <div class="alert alert-success alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
            <i class="bi bi-check-circle-fill me-2"></i> Tu producto ha sido actualizado correctamente.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${param.msg == 'respuestaExitosa'}">
        <div class="alert alert-success alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
            <i class="bi bi-check-circle-fill me-2"></i> Has respondido a la oferta exitosamente.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${param.error == 'errorActualizacion'}">
        <div class="alert alert-danger alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> Ocurrió un error al intentar actualizar el producto.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row g-4">

        <!-- ================================================================= -->
        <!-- BLOQUE IZQUIERDO: IMÁGENES + OFERTAS RECIBIDAS                  -->
        <!-- ================================================================= -->
        <div class="col-12 col-md-7 col-lg-7">

            <!-- GALERÍA DE IMÁGENES -->
            <div class="row g-3">
                <!-- COLUMNA 1: MINIATURAS -->
                <div class="col-12 col-md-4 col-lg-3">
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
                <div class="col-12 col-md-8 col-lg-9">
                    <img id="imgPrincipal"
                         src="${not empty listaImagenes ? listaImagenes[0] : 'assets/img/icono-integradora.jpeg'}"
                         class="main-detail-img w-100 rounded shadow-sm" alt="${producto.nombre}">
                </div>
            </div>

            <!-- SECCIÓN INFERIOR IZQUIERDA: OFERTAS RECIBIDAS -->
            <hr class="my-4">
            <div class="row">
                <div class="col-12">
                    <div class="d-flex align-items-center justify-content-between mb-4">
                        <h3 class="fw-bold m-0">
                            <i class="bi bi-inbox-fill text-warning me-2"></i>Ofertas Recibidas
                        </h3>
                        <span class="badge bg-secondary fs-6 px-3 py-2 rounded-pill">
                            Total: ${not empty listaOfertasRecibidas ? listaOfertasRecibidas.size() : 0}
                        </span>
                    </div>

                    <c:choose>
                        <c:when test="${not empty listaOfertasRecibidas}">
                            <div class="row g-3">
                                <c:forEach var="oferta" items="${listaOfertasRecibidas}">
                                    <div class="col-12 col-sm-6">
                                        <div class="card h-100 border-0 shadow-sm p-3" style="border-radius: 15px; background-color: #ffffff;">
                                            <div class="card-body p-2">
                                                <div class="d-flex justify-content-between align-items-center mb-2">
                                                    <h4 class="fw-bold text-success mb-0">
                                                        <fmt:formatNumber currencySymbol="$" value="${oferta.montoOferta}" type="currency"/>
                                                    </h4>
                                                </div>

                                                <p class="text-muted small mb-3">
                                                    <i class="bi bi-person-fill text-secondary me-1"></i>Ofertante:
                                                    <strong class="text-dark">${oferta.nombreComprador}</strong>
                                                </p>

                                                <div class="d-flex justify-content-end align-items-center pt-2 border-top">
                                                    <c:choose>
                                                        <%-- Estado 0: Pendiente --%>
                                                        <c:when test="${oferta.estado == 0}">
                                                            <div class="d-flex gap-2">
                                                                <a href="responderOferta?id=${oferta.idOferta}&accion=aceptar&idProducto=${producto.idProducto}&origen=detalle"
                                                                   class="btn btn-success btn-sm px-3 fw-bold rounded-pill">
                                                                    <i class="bi bi-check-lg me-1"></i> Aceptar
                                                                </a>
                                                                <a href="responderOferta?id=${oferta.idOferta}&accion=rechazar&idProducto=${producto.idProducto}&origen=detalle"
                                                                   class="btn btn-outline-danger btn-sm px-3 fw-bold rounded-pill">
                                                                    <i class="bi bi-x-lg me-1"></i> Rechazar
                                                                </a>
                                                            </div>
                                                        </c:when>
                                                        <%-- Estado 1: Aceptada --%>
                                                        <c:when test="${oferta.estado == 1}">
                                                            <span class="badge bg-success px-3 py-2 rounded-pill fs-6">
                                                                <i class="bi bi-check-circle-fill me-1"></i> Aceptada
                                                            </span>
                                                        </c:when>
                                                        <%-- Estado 2: Rechazada --%>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary px-3 py-2 rounded-pill fs-6">
                                                                <i class="bi bi-x-circle me-1"></i> Rechazada
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-light border text-center p-5 shadow-sm" style="border-radius: 15px;">
                                <i class="bi bi-inbox fs-1 text-muted d-block mb-3"></i>
                                <h5 class="text-secondary fw-bold">Aún no has recibido ofertas</h5>
                                <p class="text-muted mb-0">Cuando un comprador envíe una propuesta económica por tu producto, aparecerá listada en esta sección.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div>

        <!-- ================================================================= -->
        <!-- BLOQUE DERECHO: FORMULARIO DE EDICIÓN                             -->
        <!-- ================================================================= -->
        <div class="col-12 col-md-5 col-lg-5 ps-lg-4">
            <div class="card border-0 shadow-sm p-4 edit-modal-content">
                <h4 class="edit-modal-title mb-4"><i class="bi bi-pencil-fill me-2"></i>Editar mi Producto</h4>

                <form id="formEditarProducto" action="editarProducto" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="idProducto" value="${producto.idProducto}">

                    <div class="mb-3 text-start">
                        <label class="edit-field-label">Título del Producto</label>
                        <div class="edit-input-group">
                            <input type="text" name="nombre" class="edit-input w-100" value="${producto.nombre}" required>
                        </div>
                    </div>

                    <div class="mb-3 text-start">
                        <label class="edit-field-label">Descripción</label>
                        <div class="edit-input-group">
                            <textarea name="descripcion" class="edit-input w-100" rows="4" required>${producto.descripcion}</textarea>
                        </div>
                    </div>

                    <div class="mb-3 text-start">
                        <label class="edit-field-label">Precio ($)</label>
                        <div class="edit-input-group d-flex align-items-center px-2">
                            <span class="text-secondary fw-bold pe-2">$</span>
                            <input type="number" step="0.01" name="precio" class="edit-input flex-grow-1 border-0 px-0" style="outline: none; box-shadow: none; background: transparent;" value="${producto.precio}" required>
                        </div>
                    </div>

                    <div class="mb-3 text-start">
                        <label class="edit-field-label">Categoría</label>
                        <div class="edit-input-group">
                            <select name="idCategoria" id="categoria" class="edit-input w-100" required style="background: transparent; border: none; outline: none; cursor: pointer;">
                                <option value="" disabled>Selecciona la categoría</option>
                                <c:forEach var="cat" items="${listaCategorias}">
                                    <option value="${cat.idCategoria}" ${producto.idCategoria == cat.idCategoria ? 'selected' : ''}>
                                            ${cat.nombreCategoria}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="mb-4 text-start">
                        <label class="edit-field-label">Nuevas Imágenes (Opcional - Máx 3)</label>
                        <div class="edit-input-group">
                            <input type="file" name="imagenes" class="edit-input w-100" style="padding-top: 0.4rem;" accept="image/png, image/jpeg, image/webp" multiple>
                        </div>
                        <div class="form-text text-danger mt-2 small">
                            <i class="bi bi-exclamation-triangle"></i> Si subes nuevas fotos, estas <strong>reemplazarán</strong> a las actuales.
                        </div>
                    </div>

                    <div class="d-flex justify-content-end mt-4">
                        <button type="button" class="btn btn-guardar-edit w-100" data-bs-toggle="modal" data-bs-target="#confirmModalProducto" disabled>
                            Guardar Cambios
                        </button>
                    </div>
                </form>
            </div>
        </div>

    </div>

</div>

<!-- MODAL DE CONFIRMACIÓN -->
<div class="modal fade" id="confirmModalProducto" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content custom-modal-content">
            <div class="modal-body p-4 text-center">
                <p class="modal-text mb-0">
                    Estás por <strong>guardar los cambios</strong> de este producto.<br>¿Estás seguro?
                </p>
                <div class="d-flex justify-content-between mt-4 px-2">
                    <button type="button" class="btn btn-cancelar" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" id="btnConfirmActionProducto" class="btn btn-confirmar">Confirmar</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="assets/js/detalles-productos.js"></script>
<%@ include file="layout/footer.jsp" %>