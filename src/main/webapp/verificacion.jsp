<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verificar cuenta</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">
    <link rel="stylesheet" href="assets/css/estilos-token.css">
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
                <form action="Verificar" method="POST" class="text-center">

                    <div class="mb-2">
                        <i class="bi bi-shield-check avatar-icon"></i>
                    </div>

                    <h5 class="fw-bold mb-2 text-black">Verifica tu cuenta</h5>

                    <p class="text-muted mb-4 px-2" style="font-size: 13px;">
                        Ingresa el código de verificación de 6 dígitos que enviamos a tu correo institucional.
                    </p>

                    <div class="code-inputs-container mb-4">
                        <input type="text" name="c1" class="code-box" maxlength="1" required autocomplete="off" autofocus>
                        <input type="text" name="c2" class="code-box" maxlength="1" required autocomplete="off">
                        <input type="text" name="c3" class="code-box" maxlength="1" required autocomplete="off">
                        <input type="text" name="c4" class="code-box" maxlength="1" required autocomplete="off">
                        <input type="text" name="c5" class="code-box" maxlength="1" required autocomplete="off">
                        <input type="text" name="c6" class="code-box" maxlength="1" required autocomplete="off">
                        <input type="text" name="c7" class="code-box" maxlength="1" required autocomplete="off">
                        <input type="text" name="c8" class="code-box" maxlength="1" required autocomplete="off">
                    </div>

                    <div class="mb-3">
                        <button type="submit" class="btn btn-iniciar shadow-sm">Verificar</button>
                    </div>

                    <p class="footer-text mb-0">
                        ¿No recibiste el código? <a href="ReenviarCodigoServlet">Reenviar</a> <span id="contador" class="timer-text">15:00</span>
                    </p>

                </form>
            </div>
        </div>

    </div>
</div>

<script src="assets/js/bootstrap.js"></script>

<script src="assets/js/token.js"></script>
</body>
</html>