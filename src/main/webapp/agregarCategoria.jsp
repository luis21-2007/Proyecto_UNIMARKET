<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agregar Categoría</title>

    <!-- Mismos estilos base que el login -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos-login.css">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">

    <style>
        .add-card {
            background-color: transparent;
            border: 1.5px solid #F0A515;
            border-radius: 16px;
            padding: 40px 35px;
            max-width: 440px;
            width: 100%;
            margin: 0 auto;
        }

        .card-title-text {
            font-weight: 800;
            font-size: 1.25rem;
            color: #000;
            line-height: 1.3;
        }

        /* Botón Subir Categoría  */
        .btn-subir {
            background-color: #8B0000;
            color: #ffffff;
            border: none;
            border-radius: 25px;
            padding: 8px 40px;
            font-weight: bold;
            font-size: 16px;
            transition: background 0.2s;
        }
        .btn-subir:hover {
            background-color: #660000;
            color: #ffffff;
        }

        /* Botón Atrás */
        .btn-atras {
            background-color: #F0A515;
            color: #000;
            font-weight: bold;
            border-radius: 10px;
            padding: 8px 35px;
            font-size: 1.1rem;
            box-shadow: 0 4px 6px rgba(0,0,0,0.15);
            text-decoration: none;
            display: inline-block;
        }
        .btn-atras:hover {
            background-color: #d4900f;
            color: #000;
        }

        /* Alerta de validación */
        .error-msg {
            display: none;
            color: #dc3545;
            font-size: 0.9rem;
            font-weight: bold;
            margin-top: 5px;
        }
    </style>
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

        <!-- Columna Derecha: tarjeta de agregar categoría -->
        <div class="col-12 col-md-6">
            <div class="add-card shadow-sm text-center">
                <h5 class="card-title-text mb-4">Ingresa una categoría para<br>administrar los productos</h5>

                <form id="categoriaForm" onsubmit="validarFormulario(event)">

                    <div class="custom-input-group mb-1">
                        <div class="input-addon">
                            <i class="bi bi-box-seam"></i>
                        </div>
                        <input type="text" id="nombreCategoria" name="categoria" class="custom-input" placeholder="Ingresa la categoria">
                    </div>

                    <!-- Mensaje de error oculto por defecto -->
                    <div id="mensajeError" class="error-msg text-start mb-4">
                        <i class="bi bi-exclamation-circle"></i> No se puede subir sin completar el campo.
                    </div>

                    <!-- Espaciador si no hay error -->
                    <div class="mb-4" id="espaciador"></div>

                    <button type="submit" class="btn btn-subir">Subir Categoria</button>
                </form>
            </div>
        </div>

    </div>

    <!-- Botón Atrás anclado a la cuadrícula inferior -->
    <div class="row mt-5">
        <div class="col-12 ps-lg-5 text-center text-md-start">
            <a href="gestionCategorias.jsp" class="btn-atras">Atras</a>
        </div>
    </div>
</div>

<script src="assets/js/bootstrap.js"></script>

<script>
    function validarFormulario(event) {
        event.preventDefault();

        const inputCategoria = document.getElementById('nombreCategoria');
        const mensajeError = document.getElementById('mensajeError');
        const espaciador = document.getElementById('espaciador');

        if (inputCategoria.value.trim() === '') {
            mensajeError.style.display = 'block';
            espaciador.style.display = 'none';
        } else {
            mensajeError.style.display = 'none';
            window.location.href = 'gestionCategorias.jsp';
        }
    }
</script>

</body>
</html>
