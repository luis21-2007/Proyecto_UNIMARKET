<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Categorías</title>

    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/bootstrap-icons-1.13.1/bootstrap-icons.min.css">
    <link rel="stylesheet" href="assets/css/estilos-gestionCategorias.css">
</head>
<body>

<!-- Inclusión del header de Administrador -->
<%@ include file="layout/header_admin.jsp" %>

<div class="container mt-4 mb-5">

    <!-- Título y Botón Nueva Categoría -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="page-title">Gestion de Categorias</h1>
        <a href="agregarCategoria.jsp" class="btn btn-new-category">+ Nueva Categoría</a>
    </div>

    <!-- Tabla de Categorías -->
    <div class="table-container shadow-sm">
        <div class="table-header-main">
            Todas las Categorias
        </div>
        <table class="table custom-table">
            <thead>
            <tr>
                <th style="width: 50%;">Nombre de Categoria</th>
                <th style="width: 25%; text-align: center;">Productos</th>
                <th style="width: 25%; text-align: center;">Acciones</th>
            </tr>
            </thead>
            <tbody>
            <!-- Fila 1 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Electronicos</td>
                <td class="text-center">25</td>
                <td class="text-center">
                    <a href="editCategoria.jsp" title="Editar categoria" class="text-decoration-none">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </a>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            <!-- Fila 2 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Accesorios</td>
                <td class="text-center">15</td>
                <td class="text-center">
                    <a href="editCategoria.jsp" title="Editar categoria" class="text-decoration-none">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </a>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            <!-- Fila 3 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Compus</td>
                <td class="text-center">1</td>
                <td class="text-center">
                    <a href="editCategoria.jsp" title="Editar categoria" class="text-decoration-none">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </a>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            <!-- Fila 4 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Comida</td>
                <td class="text-center">50</td>
                <td class="text-center">
                    <a href="editCategoria.jsp" title="Editar categoria" class="text-decoration-none">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </a>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            <!-- Fila 5 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Chetos</td>
                <td class="text-center">2</td>
                <td class="text-center">
                    <a href="editCategoria.jsp" title="Editar categoria" class="text-decoration-none">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </a>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            <!-- Fila 6 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Joyeria</td>
                <td class="text-center">5</td>
                <td class="text-center">
                    <a href="editCategoria.jsp" title="Editar categoria" class="text-decoration-none">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </a>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            </tbody>
        </table>

        <!-- Paginación -->
        <div class="table-footer">
            <div>Pagina 1 de 2</div>
            <div class="pagination-controls">
                <span><i class="bi bi-chevron-left"></i></span>
                <span class="active-page">1</span>
                <span>2</span>
                <span>3</span>
                <span><i class="bi bi-chevron-right"></i></span>
            </div>
        </div>
    </div>

</div>

<!-- Modal de Confirmación -->
<div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content custom-modal-content">
            <div class="modal-body p-0">
                <p class="modal-text" id="modalDynamicText">
                    Estas por deshabilitar la categoría<br>¿Estas seguro?
                </p>
                <div class="d-flex justify-content-between mt-4 px-2">
                    <button type="button" class="btn btn-cancelar" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-confirmar" id="btnConfirmAction">Confirmar</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="assets/js/bootstrap.bundle.min.js"></script>

<!-- Script para manejar la lógica de los botones Toggle -->
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
        const modalText = document.getElementById('modalDynamicText');
        const btnConfirmAction = document.getElementById('btnConfirmAction');

        let currentToggleButton = null;

        document.querySelectorAll('.toggle-btn').forEach(button => {
            button.addEventListener('click', function () {
                currentToggleButton = this;
                const currentState = this.getAttribute('data-state');

                if (currentState === 'on') {
                    modalText.innerHTML = "Estas por deshabilitar la categoría<br>¿Estas seguro?";
                } else {
                    modalText.innerHTML = "Estas por habilitar la categoría<br>¿Estas seguro?";
                }

                confirmModal.show();
            });
        });

        btnConfirmAction.addEventListener('click', function () {
            if (currentToggleButton) {
                const currentState = currentToggleButton.getAttribute('data-state');

                if (currentState === 'on') {
                    currentToggleButton.classList.remove('bi-toggle-on', 'text-green');
                    currentToggleButton.classList.add('bi-toggle-off', 'text-red');
                    currentToggleButton.setAttribute('data-state', 'off');
                } else {
                    currentToggleButton.classList.remove('bi-toggle-off', 'text-red');
                    currentToggleButton.classList.add('bi-toggle-on', 'text-green');
                    currentToggleButton.setAttribute('data-state', 'on');
                }
            }

            confirmModal.hide();
        });
    });
</script>
</body>
</html>
