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
</head>
<body>

<header class="custom-header d-flex align-items-center justify-content-between pe-4">

    <div class="logo-container d-flex align-items-center h-100">
        <a href="index.jsp" class="d-inline-block">
            <img src="assets/img/icono-integradora.jpeg" alt="Logo" class="logo-img rounded-circle">
        </a>
    </div>
    <div class="search-container flex-grow-1 mx-4" style="max-width: 600px;">
        <div class="input-group">
            <input type="text" class="form-control" placeholder="Busca tus productos favoritos" aria-label="Buscar">
            <button class="btn btn-search" type="button">
                <i class="bi bi-search text-white"></i>
            </button>
        </div>
    </div>

    <div class="d-flex align-items-center gap-4">

        <div class="d-flex flex-column align-items-center" >
            <a href="login.jsp" class="icon-btn mb-2">
                <span class="notification-badge"></span>
                <i class="bi bi-person-circle"></i>
            </a>
            <span class="icon-label" style="min-height: 2.2em; display: block;">Perfil<br></span>
        </div>

        <div class="d-flex flex-column align-items-center">
            <a href="#" class="icon-btn mb-2">
                <i class="bi bi-plus-lg"></i>
            </a>
            <span class="icon-label" style="min-height: 2.2em; display: block;">Agregar<br>Producto</span>
        </div>

    </div>
</header>
<main class="flex-grow-1 mt-5 mb-5">