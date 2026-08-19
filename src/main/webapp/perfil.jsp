<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="layout/header.jsp" %>

<link href="assets/css/perfil-estilos.css" rel="stylesheet">

<div class="container-fluid py-4 profile-main-container">
    <div class="container">

        <!-- Mensajes de Feedback -->
        <c:if test="${param.msg == 'actualizado'}">
            <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
                ¡Información actualizada correctamente!
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
                        <a href="misCompras" class="profile-menu-item">
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

            <!-- CONTENIDO PRINCIPAL -->
            <div class="col-md-8 col-lg-9 ps-md-4">

                <!-- Encabezado -->
                <h1 class="profile-content-title mb-0">Ajustes</h1>
                <br>

                <!-- Formulario de Ajustes -->
                <form id="formPerfil" action="perfil" method="POST">

                    <!-- SECCIÓN: INFORMACIÓN DE CUENTA -->
                    <div class="mb-5">
                        <h3 class="profile-section-heading mb-4">Información de cuenta</h3>

                        <!-- Nombre de Usuario -->
                        <div class="mb-3">
                            <label for="inputUsuario" class="profile-field-label">Nombre de Usuario</label>
                            <div class="profile-input-group">
                                <input type="text" id="inputUsuario" name="nombre" class="form-control profile-input"
                                       value="${not empty sessionScope.usuario ? sessionScope.usuario.nombre : 'Kex'}" required>
                            </div>
                        </div>

                        <!-- Correo Institucional -->
                        <div class="mb-3">
                            <label for="inputCorreo" class="profile-field-label">Dirección de Correo Institucional</label>
                            <div class="profile-input-group">
                                <input type="email" id="inputCorreo" name="correo" class="form-control profile-input"
                                       value="${not empty sessionScope.usuario ? sessionScope.usuario.correo : 'Kex@utez.edu.mx'}" required readonly>
                            </div>
                        </div>
                    </div>

                    <!-- SECCIÓN: NÚMERO DE TELÉFONO -->
                    <div class="mb-4">
                        <h3 class="profile-section-heading mb-1">Número de Teléfono</h3>
                        <p class="text-muted fw-semibold small mb-3">Administra tu número de teléfono e información de contacto.</p>

                        <div>
                            <label for="inputTelefono" class="profile-field-label">Número</label>
                            <div class="profile-input-group">
                                <input type="text" id="inputTelefono" name="telefono" class="form-control profile-input"
                                       value="${not empty sessionScope.usuario.telefono ? sessionScope.usuario.telefono : '+52 *** *** ** 63'}">
                            </div>
                        </div>
                    </div>

                    <!-- Botón Guardar Cambios (Abre Modal) -->
                    <div class="btn-guardar-perfil-wrapper mb-5">
                        <button type="button" class="btn btn-guardar-perfil" data-bs-toggle="modal" data-bs-target="#modalGuardarCambios">
                            Guardar Cambios
                        </button>
                    </div>

                </form>

                <hr class="my-5">

                <!-- OPCIÓN DE BAJA DE CUENTA SIN TARJETA -->
                <div class="mb-4">
                    <p class="text-muted small mb-3">
                        Si desactivas tu cuenta, no podrás volver a ingresar al sistema de forma inmediata.
                    </p>
                    <button type="button" class="btn btn-outline-danger btn-sm rounded-pill fw-bold" data-bs-toggle="modal" data-bs-target="#modalBajaCuenta">
                        <i class="bi bi-person-x-fill me-1"></i> Dar de baja mi cuenta
                    </button>
                </div>

            </div>

        </div>
    </div>
</div>

<!-- ==========================================
MODAL: CONFIRMAR GUARDAR CAMBIOS
========================================== -->
<div class="modal fade" id="modalGuardarCambios" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-header border-0 pb-0">
                <h5 class="modal-title fw-bold">¿Guardar cambios?</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-muted">
                ¿Estás seguro de que deseas actualizar la información de tu perfil?
            </div>
            <div class="modal-footer border-0">
                <button type="button" class="btn btn-light rounded-pill px-4" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-success rounded-pill px-4" onclick="document.getElementById('formPerfil').submit();">
                    Confirmar y Guardar
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="modalBajaCuenta" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-body text-center p-4">
                <div class="mb-3">
                    <i class="bi bi-exclamation-triangle-fill text-danger display-4"></i>
                </div>
                <h5 class="fw-bold mb-3">¿Estás seguro que quieres dar de baja tu cuenta?</h5>
                <p class="text-secondary small mb-4">
                    Ya no podrás activarla a menos de que mandes un correo al administrador: <br>
                    <a href="mailto:20253ds069@utez.edu.mx" class="fw-bold text-decoration-none">20253ds069@utez.edu.mx</a>
                </p>
                <div class="d-flex justify-content-center gap-2">
                    <button type="button" class="btn btn-light rounded-pill px-4 fw-semibold" data-bs-dismiss="modal">Cancelar</button>
                    <a href="darDeBajaCuenta" class="btn btn-danger rounded-pill px-4 fw-semibold">
                        Sí, dar de baja
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>