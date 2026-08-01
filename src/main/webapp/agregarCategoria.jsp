<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agregar Categoría</title>

    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos-login.css">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">
    <link rel="stylesheet" href="assets/css/agregar-categorias.css">
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

                <%-- Alerta de error proveniente del Servlet --%>
                <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger py-2 px-3 mb-3 text-start" style="font-size: 15px; border-radius: 8px;">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> <%= request.getAttribute("error") %>
                </div>
                <% } %>

                <h5 class="card-title-text mb-4">Ingresa una categoría para<br>administrar los productos</h5>

                <form action="categorias" method="POST" id="categoriaForm">
                    <!-- Acción requerida por CategoriaServlet -->
                    <input type="hidden" name="action" value="create">
                    <div class="custom-input-group mb-4">
                        <div class="input-addon">
                            <i class="bi bi-box-seam"></i>
                        </div>
                        <input type="text" id="nombreCategoria" name="nombreCategoria" class="custom-input" placeholder="Ingresa la categoría" value="${param.nombreCategoria}" maxlength="40" pattern="[a-zA-ZÁÉÍÓÚáéíóúÑñ\s]{2,40}" required>
                    </div>

                    <button type="submit" class="btn btn-subir">Subir Categoría</button>
                </form>
            </div>
        </div>

    </div>
    <div class="row mt-5">
        <div class="col-12 ps-lg-5 text-center text-md-start">
            <a href="gestionCategorias.jsp" class="btn-atras">Atrás</a>
        </div>
    </div>
</div>

<script src="assets/js/bootstrap.js"></script>
<script src="assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>