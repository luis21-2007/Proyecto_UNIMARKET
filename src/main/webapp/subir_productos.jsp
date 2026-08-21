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
                <!-- CONTENEDOR PARA ALERTAS DINÁMICAS DE JAVASCRIPT -->
                <div id="alertaJS" class="alert alert-danger py-2 px-3 mb-3 text-start d-none" style="font-size: 15px; border-radius: 8px;">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <span id="mensajeAlertaJS"></span>
                </div>
                <!-- ALERTA DE ERROR SI VIENE DEL SERVLET -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger py-2 px-3 mb-3 text-start" style="font-size: 15px; border-radius: 8px;">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                    </div>
                </c:if>

                <form action="subirProducto" method="POST" enctype="multipart/form-data" id="formSubirProducto" class="text-center">
                    <div class="mb-3">
                        <label for="input-file" class="btn btn-iniciar" style="cursor: pointer; padding: 6px 20px; font-size: 14px;">
                            <i class="bi bi-plus-lg me-1"></i> Subir 3 imágenes
                        </label>
                        <%-- Mantener los id tal cual para la sincronización con JS --%>
                        <input type="file" id="input-file" name="imagenes" accept="image/png, image/jpeg, image/jpg, image/webp" multiple style="display: none;"  onchange="previewImages(event)">
                        <p id="file-count" class="footer-text mt-2 mb-2" style="font-weight: bold; color: #8B0000;"></p>

                        <%-- Se agrega flex-wrap para que se acomoden correctamente en celulares --%>
                        <div id="preview-container" class="d-flex justify-content-center flex-wrap gap-2 mt-2"></div>
                    </div>
                    <p class="footer-text mb-4 text-center">
                        Procura que tus imágenes sean iguales a tu producto real.
                    </p>
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-box-seam"></i>
                        </div>
                        <input type="text" name="nombre_producto" value="${param.nombre_producto}" class="custom-input" placeholder="Ingresa el nombre del producto" maxlength="20" pattern="^[a-zA-Z0-9áéíóúÁÉÍÓÚÑñ\s\-\.\#]{3,60}$" required>
                    </div>
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-grid-3x3-gap"></i>
                        </div>
                        <select name="categoria" class="custom-input" style="color: #555;" required>
                            <option value="" disabled ${empty param.categoria ? 'selected' : ''}>Selecciona la categoría del producto</option>
                            <c:forEach var="cat" items="${listaCategorias}">
                                <option value="${cat.idCategoria}" ${param.categoria == cat.idCategoria ? 'selected' : ''}>${cat.nombreCategoria}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-tags"></i>
                        </div>
                        <input type="number" step="0.50" min="1" max="99999" name="precio" value="${param.precio}" class="custom-input" placeholder="Ingresa el precio del producto ($)" onkeypress="return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 46" maxlength="10" required>
                    </div>
                    <div class="custom-input-group mb-3">
                        <div class="input-addon">
                            <i class="bi bi-card-text"></i>
                        </div>
                        <input type="text" name="descripcion" value="${param.descripcion}" class="custom-input" placeholder="Ingresa la descripción del producto" maxlength="50" required>
                    </div>
                    <div class="mt-4">
                        <button type="submit" id="btnSubirProducto" class="btn btn-iniciar">
                            Subir producto
                        </button>
                    </div>

                </form>

            </div>
        </div>

    </div>
</div>
<script src="assets/js/imagen-producto.js?v=2.0"></script>
<script src="assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>