<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restablecer Contraseña</title>
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
                <form action="restablecer" method="POST" class="text-center" novalidate>
                    <input type="hidden" name="correo" value="<%= request.getParameter("correo") != null ? request.getParameter("correo") : "" %>">
                    <div class="mb-2">
                        <i class="bi bi-key-fill avatar-icon"></i>
                    </div>

                    <h5 class="fw-bold mb-2 text-black">Nueva contraseña</h5>

                    <p class="text-muted mb-4 px-2" style="font-size: 13px;">
                        Ingresa el código que te enviamos junto con tu nueva contraseña para actualizar tu cuenta.
                    </p>
                    <p class="text-muted mb-4 px-2" style="font-size: 13px;">
                        La nueva contraseña debe contener mínimo 8 caracteres y al menos una mayúscula.
                    </p>
                    <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger py-2 px-3 mb-3 text-start" style="font-size: 15px; border-radius: 8px;">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i> <%= request.getAttribute("error") %>
                    </div>
                    <% } %>
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-shield-check"></i>
                        </div>
                        <input type="text" value="${param.codigo}" name="codigo" class="custom-input text-uppercase" placeholder="Código de 6 dígitos" maxlength="6" required autocomplete="off">
                    </div>

                    <div class="custom-input-group mb-1 position-relative">
                        <div class="input-addon">
                            <i class="bi bi-lock"></i>
                        </div>
                        <input type="password" id="txtContra1" name="contra1" class="custom-input pe-5" placeholder="Nueva contraseña" minlength="8" maxlength="12" pattern="(?=.*[A-Z]).{8,12}" title="La contraseña debe tener al menos 8 caracteres y una letra mayúscula" required>
                        <i class="bi bi-eye position-absolute top-50 end-0 translate-middle-y me-3" style="cursor: pointer; z-index: 10;" onclick="const input = document.getElementById('txtContra1'); input.type = input.type === 'password' ? 'text' : 'password'; this.classList.toggle('bi-eye'); this.classList.toggle('bi-eye-slash');"></i>
                    </div>

                    <div>
                    </div>

                    <div class="custom-input-group mb-4 position-relative">
                        <div class="input-addon">
                            <i class="bi bi-lock-fill"></i>
                        </div>
                        <input type="password" id="txtContra2" name="contra2" class="custom-input pe-5" placeholder="Confirmar contraseña" minlength="8" maxlength="12" pattern="(?=.*[A-Z]).{8,12}" title="La contraseña debe tener al menos 8 caracteres y una letra mayúscula" required>
                        <i class="bi bi-eye position-absolute top-50 end-0 translate-middle-y me-3" style="cursor: pointer; z-index: 10;" onclick="const input = document.getElementById('txtContra2'); input.type = input.type === 'password' ? 'text' : 'password'; this.classList.toggle('bi-eye'); this.classList.toggle('bi-eye-slash');"></i>
                    </div>

                    <div class="mb-3">
                        <button type="submit" class="btn btn-iniciar shadow-sm w-100">Restablecer</button>
                    </div>

                    <p class="footer-text mb-0 mt-3">
                        ¿Quieres intentar ingresar? <a href="login.jsp" class="text-decoration-none">Iniciar sesión</a>
                    </p>

                </form>
            </div>
        </div>

    </div>
</div>

<script src="assets/js/bootstrap.js"></script>
</body>
</html>