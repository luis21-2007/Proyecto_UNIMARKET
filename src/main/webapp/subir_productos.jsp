<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Subir Producto - MUA</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">
    <link rel="stylesheet" href="assets/css/estilos-login.css">
</head>

<body>

<div class="container login-container" style="min-height: 100vh; display: flex; align-items: center;">
    <div class="row align-items-center g-5 w-100">

        <!-- Columna Izquierda: Logo / Marca -->
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

        <!-- Columna Derecha: Formulario de Subir Producto -->
        <div class="col-12 col-md-6">
            <!-- Usamos la clase .login-card de tu CSS global -->
            <div class="login-card shadow-sm">

                <form action="subirProducto" method="POST" enctype="multipart/form-data" class="text-center">

                    <div class="mb-3">
                        <!-- Botón Personalizado -->
                        <label for="input-file" class="btn btn-iniciar" style="cursor: pointer; padding: 6px 20px; font-size: 14px;">
                            <i class="bi bi-plus-lg me-1"></i> Subir imágenes (Máx 3)
                        </label>

                        <!-- Input Múltiple (máximo 3) -->
                        <input type="file" id="input-file" name="imagenes" accept="image/*" multiple style="display: none;" required onchange="previewImages(event)">

                        <!-- Texto que indica cuántas fotos ha seleccionado -->
                        <p id="file-count" class="footer-text mt-2 mb-2" style="font-weight: bold; color: #8B0000;"></p>

                        <!-- Contenedor de Previsualización -->
                        <div id="preview-container" class="d-flex justify-content-center gap-2 mt-2"></div>
                    </div>

                    <!-- Texto de Advertencia -->
                    <p class="footer-text mb-4 text-center">
                        Procura que tus imágenes sean iguales a tu producto.
                    </p>

                    <!-- Campos del Formulario (Usando .custom-input-group, .input-addon y .custom-input) -->

                    <!-- Nombre del Producto -->
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-box-seam"></i>
                        </div>
                        <input type="text" name="nombre_producto" class="custom-input" placeholder="Ingresa el nombre del producto" required>
                    </div>
                    <!-- Categoría (Select Dinámico) -->
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-grid-3x3-gap"></i>
                        </div>
                        <select name="categoria" class="custom-input" style="color: #555;" required>
                            <option value="" disabled selected>Selecciona la categoría del producto</option>
                            <!-- Se itera con JSTL si mandas la lista desde el Servlet -->
                            <c:forEach var="cat" items="${listaCategorias}">
                                <option value="${cat.idCategoria}">${cat.nombreCategoria}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Precio -->
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-tags"></i>
                        </div>
                        <input type="number" step="0.01" name="precio" class="custom-input" placeholder="Ingresa el precio del producto" required>
                    </div>

                    <!-- Descripción -->
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-card-text"></i>
                        </div>
                        <input type="text" name="descripcion" class="custom-input" placeholder="Ingresa la descripción del producto" required>
                    </div>

                    <!-- Botón Subir Producto -->
                    <div class="mt-4">
                        <button type="submit" class="btn btn-iniciar">
                            Subir producto
                        </button>
                    </div>

                </form>

            </div>
        </div>

    </div>
</div>
<script src="assets/js/imagen-producto.js"></script>
<script src="assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>