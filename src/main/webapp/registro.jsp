<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>creacion de cuenta</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos-registro.css">
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
                <form action="register" method="POST" class="text-center">

                    <div class="mb-2">
                        <i class="bi bi-person-circle avatar-icon"></i>
                    </div>

                    <h5 class="fw-bold mb-4 text-black">Bienvenido</h5>

                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-person"></i>
                        </div>
                        <input type="text" name="nombre" class="custom-input" placeholder="Ingresa tu Nombre " required>
                    </div>
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-person"></i>
                        </div>
                        <input type="text" name="apellido" class="custom-input" placeholder="Ingresa tu Apellido " required>
                    </div>
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-person"></i>
                        </div>
                        <input type="text" name="carrera" class="custom-input" placeholder="Ingresa tu Carrera " required>
                    </div>

                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-envelope"></i>
                        </div>
                        <input type="email" name="correo" class="custom-input" placeholder="Ingresa tu correo institucional" required>
                    </div>

                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-lock"></i>
                        </div>
                        <input type="password" name="contra1" class="custom-input" placeholder="Ingresa tu Contraseña" required>
                    </div>

                    <div class="custom-input-group mb-4">
                        <div class="input-addon">
                            <i class="bi bi-lock"></i>
                        </div>
                        <input type="password" name="contra2" class="custom-input" placeholder="Confirmar Contraseña" required>
                    </div>

                    <div class="mb-3">
                        <button type="submit" class="btn btn-iniciar shadow-sm">Crear</button>
                    </div>

                    <p class="footer-text mb-0">¿Ya tienes cuenta? <a href="login.jsp">Iniciar Sesion</a>
                    </p>

                </form>
            </div>
        </div>

    </div>
</div>
<script src="assets/js/bootstrap.js"></script>
</body>
</html>