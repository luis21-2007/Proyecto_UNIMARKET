<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar Contraseña</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">
    <link rel="stylesheet" href="assets/css/estilos-login.css">
</head>
<body>

<div class="container login-container">
    <div class="row align-items-center g-5">

        <div class="col-12 col-md-6 text-md-start text-center ps-lg-5">
            <div class="brand-title">
                <div class="mb-2">
                    <span>M</span><p>arketplace</p>
                </div>
                <div class="mb-2">
                    <span>U</span><p>niversitario de</p>
                </div>
                <div>
                    <span>A</span><p>rticulos</p>
                </div>
            </div>
        </div>


        <div class="col-12 col-md-6">
            <div class="login-card shadow-sm">
                <form action="recuperar" method="POST" class="text-center">

                    <div class="mb-2">
                        <i class="bi bi-shield-lock avatar-icon"></i>
                    </div>

                    <h5 class="fw-bold mb-2 text-black">¿Olvidaste tu contraseña?</h5>

                    <p class="text-muted mb-4 px-2" style="font-size: 13px;">
                        Ingresa tu correo institucional registrado y te enviaremos las instrucciones de recuperación.
                    </p>

                    <!-- Mensajes de alerta con el estilo del login -->
                    <% if (request.getAttribute("mensaje") != null) { %>
                    <div class="alert alert-info py-2 px-3 mb-3 text-start" style="font-size: 12px; border-radius: 8px;">
                        <i class="bi bi-info-circle-fill me-2"></i> <%= request.getAttribute("mensaje") %>
                    </div>
                    <% } %>

                    <!-- Campo de entrada para el correo (mismos estilos que tu login) -->
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-envelope"></i>
                        </div>
                        <input type="email" name="correo" class="custom-input" placeholder="Ingresa tu correo institucional" required>
                    </div>


                    <div class="mb-3">
                        <button type="submit" class="btn btn-iniciar shadow-sm w-100">Enviar</button>
                    </div>

                    <p class="footer-text mb-0 mt-3">
                        ¿Recordaste tu contraseña? <a href="login.jsp" class="text-decoration-none">Iniciar sesión</a>
                    </p>

                </form>
            </div>
        </div>

    </div>
</div>

<script src="assets/js/bootstrap.js"></script>
</body>
</html>