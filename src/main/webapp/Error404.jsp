<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%
    // Aseguramos que la respuesta HTTP sea 404
    response.setStatus(500);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Marketplace - Error 404</title>

    <!-- Archivos CSS de tu proyecto -->
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link href="assets/css/bi/bootstrap-icons.min.css" rel="stylesheet">
    <link href="assets/css/estilos-personalizados.css" rel="stylesheet">
    <link href="assets/css/estilos-index.css" rel="stylesheet">
    <link href="assets/css/detalles-productos.css" rel="stylesheet">
</head>
<body class="d-flex flex-column min-vh-100">

<!-- Contenido Principal: Error 404 -->
<main class="flex-grow-1 d-flex flex-column align-items-center justify-content-center text-center p-4">
    <div class="container d-flex flex-column align-items-center">

        <!-- Ilustración del gatito -->
        <img src="assets/img/Pato 404.png" alt="Error 404 Pato" class="img-fluid mb-4" style="max-width: 240px;">

        <!-- Mensaje principal -->
        <h1 class="fw-bolder text-dark mb-3">
            <span class="text-danger">¡UPS!</span>
            <p></p>
            <span>Parece que a habido un pequeño problema al cargar la pagina </span>
        </h1>

        <!-- Descripción -->
        <p class="text-muted mb-4 fs-6" style="max-width: 650px; line-height: 1.6;">
            Algo ha salido mal en el núcleo de nuestro Marketplace Universitario. Nuestros estudiantes técnicos están trabajando en ello para solucionarlo tan pronto como sea posible.
        </p>

        <!-- Botón de regreso -->
        <a href="index.jsp" class="btn btn-warning rounded-pill px-4 py-2 fw-bold text-white shadow-sm">
            Regresar a la Página Principal
        </a>

    </div>
</main>

</body>
</html>