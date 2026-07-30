<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Subir Producto - MUA</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/estilos-login.css">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">

    <style>
        /* Estilos específicos para la vista de Subir Producto */

        /* Tarjeta del formulario */
        .product-card {
            border: 1.5px solid #f39c12; /* Borde naranja */
            border-radius: 12px;
            background-color: transparent;
            padding: 20px 30px;
        }

        /* Placeholder de la imagen */
        .img-placeholder {
            background-color: #d3d3d3;
            height: 130px;
            width: 80%;
            margin: 0 auto;
            border-radius: 10px;
            display: flex;
            align-items: flex-end;
            justify-content: center;
            padding-bottom: 10px;
            margin-bottom: 15px;
        }
        .dot {
            height: 8px;
            width: 8px;
            background-color: #fff;
            border-radius: 50%;
            display: inline-block;
            margin: 0 4px;
        }
        .dot.active {
            background-color: #5591ff; /* Azul activo */
        }

        /* Botones personalizados */
        .btn-outline-custom {
            color: #921714;
            border: 2px solid #921714;
            border-radius: 25px;
            font-weight: 600;
            padding: 5px 25px;
            background-color: white;
            transition: all 0.3s ease;
        }
        .btn-outline-custom:hover {
            background-color: #921714;
            color: white;
        }

        /* Texto de advertencia */
        .disclaimer-text {
            font-size: 0.85rem;
            font-weight: 700;
            line-height: 1.3;
            color: #000;
            margin: 15px 0 25px 0;
            text-align: center;
        }

        /* Inputs personalizados */
        .custom-input-group {
            border: 1px solid #f3bc7a; /* Borde sutil naranja */
            border-radius: 25px;
            display: flex;
            overflow: hidden;
            background: white;
            margin-bottom: 15px;
        }
        .input-addon-orange {
            background-color: #e69138; /* Fondo naranja del icono */
            color: black;
            padding: 8px 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-right: 1px solid #f3bc7a;
        }
        .input-addon-orange i {
            font-size: 1.3rem;
        }
        .custom-control {
            border: none;
            box-shadow: none;
            width: 100%;
            padding: 8px 15px;
            outline: none;
            color: #000000;
            font-size: 0.9rem;
        }
        .custom-control::placeholder {
            color: #a8a8a8;
        }
        .custom-control:focus {
            outline: none;
        }

        /* Select personalizado (flecha grande) */
        .custom-select {
            appearance: none;
            background: transparent url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3e%3cpath fill='none' stroke='%23555' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M2 5l6 6 6-6'/%3e%3c/svg%3e") no-repeat right 1rem center/20px 20px;
        }
    </style>
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
            <div class="product-card shadow-sm">

                <form action="subirProducto" method="POST" enctype="multipart/form-data" class="text-center">

                    <!-- Placeholder del Carrusel de Imagen -->
                    <div class="img-placeholder">
                        <div>
                            <span class="dot active"></span>
                            <span class="dot"></span>
                            <span class="dot"></span>
                        </div>
                    </div>

                    <!-- Botón Subir Imagen -->
                    <div class="mb-3">
                        <button type="button" class="btn btn-outline-custom">
                            <i class="bi bi-plus-lg"></i> Subir imagen
                        </button>
                    </div>

                    <!-- Texto de Advertencia -->
                    <p class="disclaimer-text">
                        Procura que tus imagenes sean iguales a tu producto.<br>
                        Evitemos publicidad engañosa ya que puede ser motivo de<br>
                        un reporte
                    </p>

                    <!-- Campos del Formulario -->

                    <!-- Nombre del Producto -->
                    <div class="custom-input-group">
                        <div class="input-addon-orange">
                            <i class="bi bi-box-seam"></i>
                        </div>
                        <input type="text" name="nombre_producto" class="custom-control" placeholder="Ingresa el nombre del producto" required>
                    </div>

                    <!-- Unidades -->
                    <div class="custom-input-group">
                        <div class="input-addon-orange">
                            <i class="bi bi-boxes"></i>
                        </div>
                        <input type="number" name="unidades" class="custom-control" placeholder="Ingresa las unidades disponibles" required>
                    </div>

                    <!-- Categoría (Select) -->
                    <div class="custom-input-group">
                        <div class="input-addon-orange">
                            <i class="bi bi-grid-3x3-gap"></i>
                        </div>
                        <select name="categoria" class="custom-control custom-select" required>
                            <option value="" disabled selected>Ingresa la categoria del producto</option>
                            <option value="1">Electrónica</option>
                            <option value="2">Libros</option>
                            <option value="3">Ropa</option>
                            <option value="4">Útiles Escolares</option>
                        </select>
                    </div>

                    <!-- Precio -->
                    <div class="custom-input-group">
                        <div class="input-addon-orange">
                            <i class="bi bi-tags"></i>
                        </div>
                        <input type="number" step="0.01" name="precio" class="custom-control" placeholder="Ingresa el precio del producto" required>
                    </div>

                    <!-- Descripción -->
                    <div class="custom-input-group">
                        <div class="input-addon-orange">
                            <i class="bi bi-card-text"></i>
                        </div>
                        <input type="text" name="descripcion" class="custom-control" placeholder="Ingresa la descripcion del producto" required>
                    </div>

                    <!-- Teléfono (WhatsApp) -->
                    <div class="custom-input-group mb-4">
                        <div class="input-addon-orange">
                            <i class="bi bi-whatsapp"></i>
                        </div>
                        <input type="tel" name="telefono" class="custom-control" placeholder="Ingresa tu numero de telefono" required>
                    </div>

                    <!-- Botón Subir Producto -->
                    <div class="mb-2">
                        <button type="submit" class="btn btn-outline-custom" style="padding: 5px 30px;">
                            Subir producto
                        </button>
                    </div>

                </form>

            </div>
        </div>

    </div>
</div>

<script src="assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>