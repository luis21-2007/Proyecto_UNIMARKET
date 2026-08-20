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
    <link href="assets/css/estilos-personalizados.css" rel="stylesheet">
    <link href="assets/css/estilos-index.css" rel="stylesheet">
    <link href="assets/css/estilos-personalizados.css?v=2.0" rel="stylesheet">
</head>
<body>

<header class="custom-header d-flex align-items-center justify-content-between pe-4">

    <!-- Logo del Marketplace -->
    <div class="logo-container d-flex align-items-center h-100">
        <a href="inicio" class="d-inline-block">
            <img src="assets/img/icono-integradora.jpeg" alt="Logo" class="logo-img rounded-circle">
        </a>
    </div>

    <!-- Barra de Búsqueda con ID asignado -->
    <div class="search-container flex-grow-1 mx-4" style="max-width: 600px;">
        <div class="input-group">
            <input type="text" id="inputBuscarProducto" class="form-control" placeholder="Busca tus productos favoritos" aria-label="Buscar" autocomplete="off">
            <button class="btn btn-search" type="button">
                <i class="bi bi-search text-white"></i>
            </button>
        </div>
    </div>

    <!-- Sección de Acciones -->
    <div class="d-flex align-items-center gap-4">

        <c:choose>
            <c:when test="${not empty sessionScope.usuario}">
                <!-- Botón de Mis Productos-->
                <div class="d-flex flex-column align-items-center">
                    <a href="misProductos" class="icon-btn mb-2" title="Ir a mis productos">
                        <i class="bi-bag-fill"></i>
                    </a>
                    <span class="icon-label" style="min-height: 2.2em; display: block;">Mis Productos<br></span>
                </div>

                <!-- Botón de Perfil -->
                <div class="d-flex flex-column align-items-center">
                    <a href="perfil" class="icon-btn mb-2" title="Ir a mi perfil">
                        <i class="bi bi-person-circle"></i>
                    </a>
                    <span class="icon-label" style="min-height: 2.2em; display: block;">Perfil<br></span>
                </div>

                <!-- Botón de Agregar Producto -->
                <div class="d-flex flex-column align-items-center">
                    <a href="subirProducto" class="icon-btn mb-2" title="Publicar producto">
                        <i class="bi bi-plus-lg"></i>
                    </a>
                    <span class="icon-label text-center" style="min-height: 2.2em; display: block;">Agregar<br>Producto</span>
                </div>

                <div class="d-flex flex-column align-items-center">
                    <a href="logout" class="icon-btn mb-2" title="Cerrar sesión">
                        <i class="bi bi-box-arrow-right"></i>
                    </a>
                    <span class="icon-label" style="min-height: 2.2em; display: block;">Salir<br></span>
                </div>

            </c:when>

            <c:otherwise>

                <div class="d-flex flex-column align-items-center">
                    <a href="login.jsp" class="icon-btn mb-2" title="Iniciar Sesión">
                        <i class="bi bi-person-circle"></i>
                    </a>
                    <span class="icon-label text-center" style="min-height: 2.2em; display: block;">Iniciar<br>Sesión</span>
                </div>

            </c:otherwise>
        </c:choose>

    </div>
</header>

<main class="flex-grow-1 mt-5 mb-5">