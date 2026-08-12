<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="layout/header.jsp" %>
<link href="${pageContext.request.contextPath}/assets/css/estilos-gestionCategorias.css?v=1.0" rel="stylesheet">
<!-- Alertas (Oferta enviada o Producto actualizado) -->
<c:if test="${param.msg == 'ofertaExitosa'}">
    <div class="alert alert-success alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-check-circle-fill me-2"></i> Tu oferta ha sido enviada al vendedor exitosamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
<c:if test="${param.msg == 'actualizacionExitosa'}">
    <div class="alert alert-info alert-dismissible fade show mt-3 shadow-sm" role="alert" style="border-radius: 10px;">
        <i class="bi bi-pencil-square me-2"></i> Tu producto ha sido actualizado correctamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<div class="container py-5">
    <div class="row g-4">

        <!-- COLUMNA 1: MINIATURAS (Visible para todos) -->
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

        <!-- COLUMNA 2: IMAGEN PRINCIPAL (Visible para todos) -->
        <div class="col-12 col-md-5 col-lg-5">
            <img id="imgPrincipal"
                 src="${not empty listaImagenes ? listaImagenes[0] : 'assets/img/icono-integradora.jpeg'}"
                 class="main-detail-img w-100 rounded shadow-sm" alt="${producto.nombre}">
        </div>

        <!-- COLUMNA 3: DETALLES O FORMULARIO DE EDICIÓN -->
        <div class="col-12 col-md-4 col-lg-5 ps-lg-4">
            <c:choose>
                <%-- ================================================================= --%>
                <%-- VISTA DUEÑO: FORMULARIO DE EDICIÓN (ESTILO CONGRUENTE)            --%>
                <%-- ================================================================= --%>
                <c:when test="${esPropietario}">
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
                                    <i class="bi bi-exclamation-triangle"></i> Si subes nuevas fotos, estas <strong>reemplazarán</strong> a las actuales. Si dejas esto vacío, se conservarán las que ya tienes.
                                </div>
                            </div>

                            <!-- Botón con el estilo congruente que dispara tu modal de confirmación -->
                            <div class="d-flex justify-content-end mt-4">
                                <button type="button" class="btn btn-guardar-edit w-100" data-bs-toggle="modal" data-bs-target="#confirmModalProducto">
                                    Guardar Cambios
                                </button>
                            </div>
                        </form>
                    </div>
                </c:when>

                <%-- AQUÍ PUEDES AGREGAR EL <c:otherwise> SI QUIERES MOSTRAR ALGO AL COMPRADOR --%>

            </c:choose>
        </div>

    </div>

    <%-- ================================================================= --%>
    <%-- CONTENEDOR DE OFERTAS (SOLO VISIBLE PARA EL DUEÑO)                --%>
    <%-- ================================================================= --%>
    <c:if test="${esPropietario}">
        <hr class="my-5">
        <div class="row">
            <div class="col-12">
                <h3 class="fw-bold mb-4"><i class="bi bi-inbox-fill text-warning me-2"></i>Ofertas Recibidas</h3>

                <c:choose>
                    <c:when test="${not empty listaOfertas}">
                        <div class="row g-3">
                            <c:forEach var="oferta" items="${listaOfertas}">
                                <div class="col-12 col-md-6 col-lg-4">
                                    <div class="card h-100 border-0 shadow-sm" style="border-radius: 15px;">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <h4 class="fw-bold text-success mb-0">
                                                    <fmt:formatNumber currencySymbol="$" value="${oferta.monto}" type="currency"/>
                                                </h4>
                                                <span class="badge bg-light text-dark border">
                                                    <fmt:formatDate value="${oferta.fecha}" pattern="dd/MM/yyyy" />
                                                </span>
                                            </div>
                                            <p class="text-muted small mb-3">
                                                Ofertante: <strong>${oferta.nombreComprador}</strong>
                                            </p>

                                            <div class="d-flex gap-2">
                                                <!-- Botón para aceptar oferta -->
                                                <form action="responderOferta" method="POST" class="flex-grow-1">
                                                    <input type="hidden" name="idOferta" value="${oferta.idOferta}">
                                                    <input type="hidden" name="accion" value="aceptar">
                                                    <button type="submit" class="btn btn-success btn-sm w-100 rounded-pill">Aceptar</button>
                                                </form>

                                                <!-- Botón para rechazar oferta -->
                                                <form action="responderOferta" method="POST" class="flex-grow-1">
                                                    <input type="hidden" name="idOferta" value="${oferta.idOferta}">
                                                    <input type="hidden" name="accion" value="rechazar">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm w-100 rounded-pill">Rechazar</button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-light border text-center p-5" style="border-radius: 15px;">
                            <i class="bi bi-emoji-frown fs-1 text-muted d-block mb-3"></i>
                            <h5 class="text-secondary">Aún no hay ofertas</h5>
                            <p class="text-muted mb-0">Cuando alguien envíe una oferta por tu producto, aparecerá aquí.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:if>

</div> <!-- Cierre del container principal -->

<!-- Modal de Confirmación para Editar Producto (Con tu diseño personalizado) -->
<div class="modal fade" id="confirmModalProducto" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content custom-modal-content">
            <div class="modal-body p-4 text-center">
                <p class="modal-text mb-0">
                    Estás por <strong>guardar los cambios</strong> de este producto.<br>¿Estás seguro?
                </p>
                <div class="d-flex justify-content-between mt-4 px-2">
                    <button type="button" class="btn btn-cancelar" data-bs-dismiss="modal">Cancelar</button>
                    <!-- Usamos el ID del script, pero con tus clases de diseño -->
                    <button type="button" id="btnConfirmActionProducto" class="btn btn-confirmar">Confirmar</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="assets/js/detalles-productos.js"></script>
<!-- Script mágico para conectar el modal con el form y evitar el doble clic -->
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const btnConfirm = document.getElementById('btnConfirmActionProducto');
        const formEditar = document.getElementById('formEditarProducto');

        if (btnConfirm && formEditar) {
            btnConfirm.addEventListener('click', function() {
                // 1. Cambiamos el texto y bloqueamos el botón para evitar que le den varios clics
                this.disabled = true;
                this.innerHTML = '<i class="bi bi-hourglass-split me-1"></i> Guardando...';

                // 2. Forzamos el envío del formulario
                formEditar.submit();
            });
        }
    });
</script>

<%@ include file="layout/footer.jsp" %>