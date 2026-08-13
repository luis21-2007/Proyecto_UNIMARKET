<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="layout/header.jsp" %>

<link href="assets/css/perfil-estilos.css" rel="stylesheet">

<div class="container-fluid py-4 profile-main-container">
    <div class="container">

        <!-- Mensaje de Feedback -->
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

                <form action="perfil" method="POST">

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

                    <div class="btn-guardar-perfil-wrapper">
                        <button type="submit" class="btn btn-guardar-perfil">
                            Guardar Cambios
                        </button>
                    </div>

                </form>

            </div>

        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>