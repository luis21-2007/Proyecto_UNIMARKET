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
                <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger py-2 px-3 mb-3 text-start" style="font-size: 15px; border-radius: 8px;">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> <%= request.getAttribute("error") %>
                </div>
                <% } %>
                <form action="register" method="POST" id="formRegistro" class="text-center" novalidate>

                    <div class="mb-2">
                        <i class="bi bi-person-circle avatar-icon"></i>
                    </div>

                    <h5 class="fw-bold mb-2 text-black">Bienvenido</h5>

                    <div class="custom-input-group mb-2">
                        <div class="input-addon">
                            <i class="bi bi-person"></i>
                        </div>
                        <input value="${param.nombre}" type="text" name="nombre" class="custom-input" placeholder="Ingresa tu Nombre" maxlength="25" pattern="[a-zA-ZÁÉÍÓÚáéíóúÑñ\s]{1,50}" required>
                    </div>
                    <div class="custom-input-group mb-2">
                        <div class="input-addon">
                            <i class="bi bi-person"></i>
                        </div>
                        <input value="${param.apellido}" type="text" name="apellido" class="custom-input" placeholder="Ingresa tu Apellido" maxlength="25" pattern="[a-zA-ZÁÉÍÓÚáéíóúÑñ\s]{1,50}" required>
                    </div>

                    <div class="custom-input-group mb-2">
                        <div class="input-addon">
                            <i class="bi bi-mortarboard"></i>
                        </div>
                        <input type="text" value="${param.carrera}" name="carrera" class="custom-input" placeholder="Elige tu carrera" list="lista-carreras" required autocomplete="off" onkeydown="return ['Backspace', 'Delete', 'Tab', 'Escape'].includes(event.key);" onpaste="return false;">
                    </div>
                    <datalist id="lista-carreras">
                        <option value="Licenciatura en Negocios y Mercadotecnia">
                        <option value="Licenciatura en Diseño Digital y Producción Audiovisual">
                        <option value="Licenciatura en Contaduría">
                        <option value="Licenciatura en Administración">
                        <option value="Licenciatura en Gestión del Bienestar">
                        <option value="Licenciatura en Terapia Física">
                        <option value="Ingeniería en Tecnologías de la Información">
                        <option value="Ingeniería en Diseño Textil y Moda">
                        <option value="Ingeniería Industrial">
                        <option value="Ingeniería Mecatrónica">
                        <option value="Ingeniería en Mantenimiento Industrial">
                        <option value="Ingeniería en Nanotecnología">
                    </datalist>
                    <div class="custom-input-group mb-2">
                        <div class="input-addon">
                            <i class="bi bi-phone"></i>
                        </div>
                        <input type="tel" value="${param.telefono}" name="telefono" class="custom-input" placeholder="Ingresa tu Celular / WhatsApp" maxlength="10" pattern="^[0-9]{10}$" onkeypress="return event.charCode >= 48 && event.charCode <= 57" required autocomplete="off">
                    </div>
                    <div class="custom-input-group mb-2">
                        <div class="input-addon">
                            <i class="bi bi-envelope"></i>
                        </div>
                        <input type="email" value="${param.correo}" name="correo" class="custom-input" placeholder="ejemplo@utez.edu.mx"
                               pattern="^[a-zA-Z0-9._%+-]+@utez\.edu\.mx$" required>
                    </div>

                    <div class="custom-input-group mb-1 position-relative">
                        <div class="input-addon">
                            <i class="bi bi-lock"></i>
                        </div>
                        <input type="password" id="txtContra1" name="contra1" class="custom-input pe-5" placeholder="Ingresa tu Contraseña" minlength="8" maxlength="12" pattern="(?=.*[A-Z]).{8,12}"  required>
                        <i class="bi bi-eye position-absolute top-50 end-0 translate-middle-y me-3" style="cursor: pointer; z-index: 10;" onclick="const input = document.getElementById('txtContra1'); input.type = input.type === 'password' ? 'text' : 'password'; this.classList.toggle('bi-eye'); this.classList.toggle('bi-eye-slash');"></i>
                    </div>
                    <div class="text-start mb-2 ps-1" style="font-size: 11px; color: #6c757d;">
                    </div>

                    <div class="custom-input-group mb-2 position-relative">
                        <div class="input-addon">
                            <i class="bi bi-lock"></i>
                        </div>
                        <input type="password" id="txtContra2" name="contra2" class="custom-input pe-5" placeholder="Confirmar Contraseña" minlength="8" maxlength="12" pattern="(?=.*[A-Z]).{8,12}" required>
                        <i class="bi bi-eye position-absolute top-50 end-0 translate-middle-y me-3" style="cursor: pointer; z-index: 10;" onclick="const input = document.getElementById('txtContra2'); input.type = input.type === 'password' ? 'text' : 'password'; this.classList.toggle('bi-eye'); this.classList.toggle('bi-eye-slash');"></i>
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
<script src="assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>