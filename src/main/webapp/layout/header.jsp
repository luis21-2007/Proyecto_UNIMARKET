<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Marketplace</title>
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link href="assets/css/bi/bootstrap-icons.min.css" rel="stylesheet">
    <link href="assets/css/estilos-index.css" rel="stylesheet">
    <link href="assets/css/estilos-personalizados.css?v=2.0" rel="stylesheet">
</head>
<body>
<!-- 1. Quitamos justify-content-between del header -->
<header class="custom-header d-flex align-items-center pe-2 pe-md-4">

    <!-- 2. Logo: Agregamos ms-2 ms-md-4 para separarlo del borde izquierdo -->
    <div class="logo-container d-flex align-items-center h-100 ms-2 ms-md-4">
        <a href="inicio" class="d-inline-block">
            <img src="assets/img/icono-integradora.jpeg" alt="Logo" class="logo-img rounded-circle">
        </a>
    </div>

    <!-- Sección de Acciones -->

    <!-- 3. Contenedor de botones: Agregamos ms-auto para empujar todo a la derecha -->
    <!-- Reducimos el gap en celulares (gap-1) y lo mantenemos en PC (gap-4) -->
    <div class="d-flex align-items-center gap-1 gap-md-4 ms-auto">
        <c:choose>
            <c:when test="${not empty sessionScope.usuario}">

                <!-- Botón de Mis Productos-->
                <div class="d-flex flex-column align-items-center px-1">
                    <a href="misProductos" class="icon-btn mb-0 mb-md-2" title="Ir a mis productos">
                        <i class="bi-bag-fill"></i>
                    </a>
                    <!-- 4. Ocultamos textos en móvil con d-none d-md-block -->
                    <span class="icon-label d-none d-md-block" style="min-height: 2.2em; display: block;">Mis Productos</span>
                </div>

                <!-- Botón de Perfil -->
                <div class="d-flex flex-column align-items-center px-1">
                    <a href="perfil" class="icon-btn mb-0 mb-md-2" title="Ir a mi perfil">
                        <i class="bi bi-person-circle"></i>
                    </a>
                    <span class="icon-label d-none d-md-block" style="min-height: 2.2em; display: block;">Perfil</span>
                </div>

                <!-- Botón de Agregar Producto -->
                <div class="d-flex flex-column align-items-center px-1">
                    <a href="subirProducto" class="icon-btn mb-0 mb-md-2" title="Publicar producto">
                        <i class="bi bi-plus-lg"></i>
                    </a>
                    <span class="icon-label text-center d-none d-md-block" style="min-height: 2.2em; display: block;">Agregar<br>Producto</span>
                </div>

                <!-- Botón de Salir -->
                <div class="d-flex flex-column align-items-center px-1">
                    <a href="logout" class="icon-btn mb-0 mb-md-2" title="Cerrar sesión">
                        <i class="bi bi-box-arrow-right"></i>
                    </a>
                    <span class="icon-label d-none d-md-block" style="min-height: 2.2em; display: block;">Salir</span>
                </div>
            </c:when>

            <%-- OPCIÓN 2: SIN SESIÓN --%>
            <c:otherwise>
                <!-- Inicio de sesión obligatorio -->
                <div class="d-flex flex-column align-items-center px-1">
                    <a href="login.jsp" class="icon-btn mb-0 mb-md-2" title="Iniciar Sesión">
                        <i class="bi bi-person-circle"></i>
                    </a>
                    <span class="icon-label text-center d-none d-md-block" style="min-height: 2.2em; display: block;">Iniciar<br>Sesión</span>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</header>