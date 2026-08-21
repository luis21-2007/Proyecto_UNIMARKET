<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />

<%@ include file="layout/header.jsp" %>
<link href="${pageContext.request.contextPath}/assets/css/estilos-gestionCategorias.css?v=2.0" rel="stylesheet">

<div class="container py-5">

    <!-- ALERTAS DE FEEDBACK -->
    <c:if test="${param.msg == 'actualizacionExitosa'}">
        <div class="alert alert-success alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
            <i class="bi bi-check-circle-fill me-2"></i> Tu producto ha sido actualizado correctamente.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.msg == 'imagenEliminada'}">
        <div class="alert alert-success alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
            <i class="bi bi-check-circle-fill me-2"></i> La imagen fue eliminada correctamente.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.msg == 'respuestaExitosa'}">
        <div class="alert alert-success alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
            <i class="bi bi-check-circle-fill me-2"></i> Has respondido a la oferta exitosamente.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.error == 'errorActualizacion' || param.msg == 'errorEliminarImagen'}">
        <div class="alert alert-danger alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> Ocurrió un error procesando la solicitud de la imagen o del producto.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- ALERTA DE BLOQUEO POR OFERTAS EXISTENTES -->
    <c:if test="${tieneOfertas}">
        <div class="alert alert-warning alert-dismissible fade show mt-3 shadow-sm border-0" role="alert" style="border-radius: 10px; background-color: #fff3cd; color: #664d03;">
            <i class="bi bi-exclamation-triangle-fill me-2 fs-5 align-middle"></i>
            <strong>Acción restringida:</strong> Este producto no se puede editar ni eliminar porque ya cuenta con ofertas recibidas.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- SECCIÓN SUPERIOR: EDICIÓN DEL PRODUCTO -->
    <div class="row g-4">

        <!-- COLUMNA 1: MINIATURAS DE LAS IMÁGENES CON BOTÓN 'X' -->
        <div class="col-12 col-md-3 col-lg-2">
            <div class="d-flex flex-row flex-md-column gap-3 justify-content-center">
                <c:choose>
                    <c:when test="${not empty listaImagenes}">
                        <c:forEach var="img" items="${listaImagenes}" varStatus="status">
                            <div class="position-relative d-inline-block">
                                <img src="${img}" class="thumb-img ${status.first ? 'active-thumb' : ''}"
                                     onclick="cambiarImagenPrincipal(this.src, this)" alt="Miniatura">

                                    <%-- Botón X usando data-url en lugar de onclick inline --%>
                                <c:if test="${not tieneOfertas}">
                                    <button type="button"
                                            class="btn btn-danger btn-sm rounded-circle position-absolute d-flex align-items-center justify-content-center shadow-sm btn-eliminar-imagen-trigger"
                                            style="top: -6px; right: -6px; width: 22px; height: 22px; font-size: 12px; font-weight: bold; border: 1px solid #fff; z-index: 10;"
                                            title="Eliminar esta imagen"
                                            data-url="eliminarImagenProducto?idProducto=${producto.idProducto}&imagenUrl=${img}">
                                        &times;
                                    </button>
                                </c:if>
                            </div>
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
                 class="main-detail-img w-100 rounded shadow-sm" alt="${producto.nombre}">
        </div>

        <!-- COLUMNA 3: FORMULARIO DE EDICIÓN Y ELIMINACIÓN -->
        <div class="col-12 col-md-4 col-lg-5 ps-lg-4">
            <div class="card border-0 shadow-sm p-4 edit-modal-content">
                <h4 class="edit-modal-title mb-4"><i class="bi bi-pencil-fill me-2"></i>Editar mi Producto</h4>

                <form id="formEditarProducto" action="editarProducto" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="idProducto" value="${producto.idProducto}">

                    <div class="mb-3 text-start">
                        <label class="edit-field-label">Título del Producto</label>
                        <div class="edit-input-group">
                            <input type="text" name="nombre" class="edit-input w-100" value="${producto.nombre}" required ${tieneOfertas ? 'disabled' : ''}>
                        </div>
                    </div>

                    <div class="mb-3 text-start">
                        <label class="edit-field-label">Descripción</label>
                        <div class="edit-input-group">
                            <textarea name="descripcion" class="edit-input w-100" rows="4" required ${tieneOfertas ? 'disabled' : ''}>${producto.descripcion}</textarea>
                        </div>
                    </div>

                    <div class="mb-3 text-start">
                        <label class="edit-field-label">Precio ($)</label>
                        <div class="edit-input-group d-flex align-items-center px-2">
                            <span class="text-secondary fw-bold pe-2">$</span>
                            <input type="number" step="0.01" name="precio" class="edit-input flex-grow-1 border-0 px-0" style="outline: none; box-shadow: none; background: transparent;" value="${producto.precio}" required ${tieneOfertas ? 'disabled' : ''}>
                        </div>
                    </div>

                    <div class="mb-3 text-start">
                        <label class="edit-field-label">Categoría</label>
                        <div class="edit-input-group">
                            <select name="idCategoria" id="categoria" class="edit-input w-100" required style="background: transparent; border: none; outline: none; cursor: pointer;" ${tieneOfertas ? 'disabled' : ''}>
                                <option value="" disabled>Selecciona la categoría</option>
                                <c:forEach var="cat" items="${listaCategorias}">
                                    <option value="${cat.idCategoria}" ${producto.idCategoria == cat.idCategoria ? 'selected' : ''}>
                                            ${cat.nombreCategoria}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <!-- CAMPO DE FOTOS CON CONTROL DE LÍMITE (MÁXIMO 3) -->
                    <div class="mb-4 text-start">
                        <label class="edit-field-label">
                            Nuevas Imágenes ${not empty listaImagenes && listaImagenes.size() >= 3 ? '(Límite de 3 alcanzado)' : '(Opcional - Máx 3)'}
                        </label>
                        <div class="edit-input-group mt-1">
                            <input type="file"
                                   name="imagenes"
                                   class="edit-input w-100"
                                   style="padding-top: 0.4rem;"
                                   accept="image/png, image/jpeg, image/webp"
                                   multiple
                            ${tieneOfertas || (not empty listaImagenes && listaImagenes.size() >= 3) ? 'disabled' : ''}>
                        </div>

                        <c:if test="${not tieneOfertas}">
                            <c:choose>
                                <c:when test="${not empty listaImagenes && listaImagenes.size() >= 3}">
                                    <div class="alert alert-danger py-2 px-3 mt-2 mb-0 small rounded-3 border-0 d-flex align-items-center" style="font-size: 13px;">
                                        <i class="bi bi-exclamation-triangle-fill me-2 fs-6"></i>
                                        <div>
                                            Has alcanzado el límite de 3 imágenes. Para subir una nueva, elimina una existente usando la <strong>X</strong> sobre la miniatura de la izquierda.
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-warning py-2 px-3 mt-2 mb-0 small rounded-3 border-0 d-flex align-items-center" style="background-color: #fff8e6; color: #8a5300; font-size: 13px;">
                                        <i class="bi bi-info-circle-fill me-2 fs-6"></i>
                                        <div>
                                            Puedes adjuntar más fotos o eliminar la deseada usando la <strong>X</strong> sobre su miniatura.
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </div>

                    <!-- BOTONES DE ACCIÓN: GUARDAR Y ELIMINAR -->
                    <div class="d-flex flex-column flex-sm-row gap-2 mt-4">
                        <button type="button" class="btn btn-outline-danger w-100 fw-bold rounded-pill" data-bs-toggle="modal" data-bs-target="#confirmEliminarModal" ${tieneOfertas ? 'disabled style="cursor: not-allowed; opacity: 0.5;"' : ''}>
                            <i class="bi bi-trash3-fill me-1"></i> Eliminar producto
                        </button>
                        <button type="button" class="btn btn-guardar-edit w-100" data-bs-toggle="modal" data-bs-target="#confirmModalProducto" ${tieneOfertas ? 'disabled style="cursor: not-allowed; opacity: 0.5;"' : 'disabled'}>
                            Guardar Cambios
                        </button>
                    </div>
                </form>
            </div>
        </div>

    </div>

    <!-- SECCIÓN INFERIOR: OFERTAS RECIBIDAS -->
    <hr class="my-5">
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
                            <div class="col-12 col-md-6 col-lg-4">
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

<!-- MODAL DE CONFIRMACIÓN GUARDAR CAMBIOS -->
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

<!-- MODAL DE CONFIRMACIÓN ELIMINAR PRODUCTO -->
<div class="modal fade" id="confirmEliminarModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content" style="border-radius: 15px;">
            <div class="modal-body p-4 text-center">
                <i class="bi bi-exclamation-triangle-fill text-danger display-4 d-block mb-3"></i>
                <h5 class="fw-bold text-dark">¿Eliminar este producto?</h5>
                <p class="text-muted small mb-0">
                    Esta acción dará de baja la publicación <strong>"${producto.nombre}"</strong> y ya no aparecerá en el catálogo.
                </p>
                <div class="d-flex justify-content-center gap-2 mt-4">
                    <button type="button" class="btn btn-outline-secondary px-4 rounded-pill fw-bold" data-bs-dismiss="modal">
                        Cancelar
                    </button>
                    <a href="eliminarProducto?id=${producto.idProducto}" class="btn btn-danger px-4 rounded-pill fw-bold">
                        Sí, eliminar
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- MODAL DE CONFIRMACIÓN ELIMINAR IMAGEN -->
<div class="modal fade" id="confirmEliminarImagenModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-sm">
        <div class="modal-content" style="border-radius: 15px;">
            <div class="modal-body p-4 text-center">
                <i class="bi bi-exclamation-triangle-fill text-danger display-4 d-block mb-3"></i>
                <h5 class="fw-bold text-dark">¿Eliminar esta imagen?</h5>
                <p class="text-muted small mb-0">
                    La foto seleccionada se removerá permanentemente de la publicación.
                </p>
                <div class="d-flex justify-content-center gap-2 mt-4">
                    <button type="button" class="btn btn-outline-secondary px-3 rounded-pill fw-bold" data-bs-dismiss="modal">
                        Cancelar
                    </button>
                    <a id="btnConfirmarEliminarImagen" href="#" class="btn btn-danger px-3 rounded-pill fw-bold">
                        Sí, eliminar
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="assets/js/detalles-productos.js?v=1.1"></script>
<%@ include file="layout/footer.jsp" %>