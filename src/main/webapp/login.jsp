<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesion</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos-login.css">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">
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
                <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger py-2 px-3 mb-3 text-start" style="font-size: 15px; border-radius: 8px;">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> <%= request.getAttribute("error") %>
                </div>
                <% } %>
                <form action="login" method="POST" class="text-center" novalidate>
                    <div class="mb-2">
                        <i class="bi bi-person-circle avatar-icon"></i>
                    </div>

                    <h5 class="fw-bold mb-4 text-black">Usuario</h5>

                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-envelope"></i>
                        </div>
                        <input type="email" value="${param.correo}" name="correo" class="custom-input" placeholder="Ingresa tu correo institucional" pattern="^[a-zA-Z0-9._%+-]+@utez\.edu\.mx$" required>
                    </div>

                    <div class="custom-input-group mb-4">
                        <div class="input-addon">
                            <i class="bi bi-lock"></i>
                        </div>
                        <input type="password" value="${param.contra}" name="contra" class="custom-input" placeholder="Ingresa tu Contraseña" maxlength="12" required>
                    </div>

                    <div class="mb-3">
                        <button type="submit" class="btn btn-iniciar shadow-sm">Iniciar</button>
                    </div>
                    <p class="footer-text mb-0">No te acuerdas de tu contraseña? <a href="recuperar.jsp">Recupera tu contraseña</a>
                    <p class="footer-text mb-0">No Tienes Cuenta? <a href="registro.jsp">Crear cuenta</a>
                    </p>

                </form>
            </div>
        </div>

    </div>
</div>

<script src="assets/js/bootstrap.js"></script>
</body>
</html>