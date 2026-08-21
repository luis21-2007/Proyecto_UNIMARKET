<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Marketplace</title>
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link href="assets/css/bi/bootstrap-icons.min.css" rel="stylesheet">
    <link href="assets/css/estilos-personalizados.css" rel="stylesheet">
    <link href="assets/css/estilos-index_admin.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos-gestionCategorias.css">
    <link rel="stylesheet" href="assets/css/estilos-gestionUsuarios.css?v=1.0">
</head>
<body>

<header class="custom-header d-flex align-items-center pe-2 pe-md-4">

    <!-- Contenedor del Logo -->
    <div class="logo-container d-flex align-items-center h-100 ms-2 ms-md-4">
        <a href="adminDashboard" class="d-inline-block">
            <!-- El CSS externo ahora controla la clase .logo-img -->
            <img src="assets/img/icono-integradora.jpeg" alt="Logo" class="logo-img rounded-circle">
        </a>
    </div>

    <!-- Contenedor de Botones (ms-auto es la magia que los empuja a la derecha) -->
    <div class="d-flex align-items-center gap-1 gap-md-4 ms-auto">

        <div class="d-flex flex-column align-items-center px-1">
            <a href="gestionUsuarios" class="icon-btn mb-0 mb-md-2">
                <i class="bi bi-people-fill"></i>
            </a>
            <span class="icon-label d-none d-md-block" style="min-height: 2.2em;">Usuarios</span>
        </div>

        <div class="d-flex flex-column align-items-center px-1">
            <a href="categorias" class="icon-btn mb-0 mb-md-2">
                <i class="bi bi-pencil-square"></i>
            </a>
            <span class="icon-label d-none d-md-block" style="min-height: 2.2em;">Categorias</span>
        </div>

        <div class="d-flex flex-column align-items-center px-1">
            <a href="gestionProductos" class="icon-btn mb-0 mb-md-2">
                <i class="bi bi-bag"></i>
            </a>
            <span class="icon-label d-none d-md-block" style="min-height: 2.2em;">Productos</span>
        </div>

        <div class="d-flex flex-column align-items-center px-1">
            <a href="adminReportes" class="icon-btn mb-0 mb-md-2">
                <i class="bi bi-journal-text"></i>
            </a>
            <span class="icon-label d-none d-md-block" style="min-height: 2.2em;">Reportes</span>
        </div>

        <div class="d-flex flex-column align-items-center px-1">
            <a href="logout" class="icon-btn mb-0 mb-md-2" title="Cerrar sesión">
                <i class="bi bi-box-arrow-right"></i>
            </a>
            <span class="icon-label d-none d-md-block" style="min-height: 2.2em;">Salir</span>
        </div>

    </div>
</header>
<main class="flex-grow-1 mt-5 mb-5">