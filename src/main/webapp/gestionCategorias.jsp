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
        <a href="nuevaCategoria.jsp" class="btn btn-new-category">+ Nueva Categoría</a>
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
                    <button type="button" class="btn-edit-trigger" title="Editar categoria"
                            data-id="electronicos" data-nombre="Electronicos" data-icono="Electronicos.jpg">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </button>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            <!-- Fila 2 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Accesorios</td>
                <td class="text-center">15</td>
                <td class="text-center">
                    <button type="button" class="btn-edit-trigger" title="Editar categoria"
                            data-id="accesorios" data-nombre="Accesorios" data-icono="Accesorios.jpg">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </button>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            <!-- Fila 3 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Compus</td>
                <td class="text-center">1</td>
                <td class="text-center">
                    <button type="button" class="btn-edit-trigger" title="Editar categoria"
                            data-id="compus" data-nombre="Compus" data-icono="Compus.jpg">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </button>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            <!-- Fila 4 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Comida</td>
                <td class="text-center">50</td>
                <td class="text-center">
                    <button type="button" class="btn-edit-trigger" title="Editar categoria"
                            data-id="comida" data-nombre="Comida" data-icono="Comida.jpg">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </button>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            <!-- Fila 5 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Chetos</td>
                <td class="text-center">2</td>
                <td class="text-center">
                    <button type="button" class="btn-edit-trigger" title="Editar categoria"
                            data-id="chetos" data-nombre="Chetos" data-icono="Chetos.jpg">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </button>
                    <i class="bi bi-toggle-on text-green toggle-btn" data-state="on"></i>
                </td>
            </tr>
            <!-- Fila 6 -->
            <tr>
                <td><i class="bi bi-grid-fill me-2"></i> Joyeria</td>
                <td class="text-center">5</td>
                <td class="text-center">
                    <button type="button" class="btn-edit-trigger" title="Editar categoria"
                            data-id="joyeria" data-nombre="Joyeria" data-icono="Joyeria.jpg">
                        <i class="bi bi-pencil-square action-icon text-dark"></i>
                    </button>
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

<!-- Modal de Editar Categoría -->
<div class="modal fade" id="editCategoriaModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content edit-modal-content">
            <div class="modal-body p-0">
                <h4 class="edit-modal-title">Editar Categoria</h4>

                <form id="editCategoriaForm">
                    <input type="hidden" id="editCategoriaId" name="categoriaId">

                    <div class="mb-3 text-start">
                        <label for="editNombreCategoria" class="edit-field-label">Nombre Categoria</label>
                        <div class="edit-input-group">
                            <input type="text" id="editNombreCategoria" name="nombre" class="edit-input" required>
                        </div>
                    </div>

                    <div class="mb-4 text-start">
                        <label class="edit-field-label">Icono Categoria</label>
                        <div class="icon-upload-group">
                            <label for="editIconoCategoria" class="icon-upload-btn" title="Subir nuevo icono">
                                <i class="bi bi-upload"></i>
                            </label>
                            <input type="file" id="editIconoCategoria" name="icono" accept="image/*" hidden>
                            <span class="icon-upload-filename" id="editIconoNombre">Sin archivo</span>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between mt-4">
                        <button type="button" class="btn btn-cancelar-edit" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-guardar-edit">Editar Categoria</button>
                    </div>
                </form>
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

        /* Modal de Editar Categoría */
        const editModal = new bootstrap.Modal(document.getElementById('editCategoriaModal'));
        const editForm = document.getElementById('editCategoriaForm');
        const editCategoriaId = document.getElementById('editCategoriaId');
        const editNombreInput = document.getElementById('editNombreCategoria');
        const editIconoInput = document.getElementById('editIconoCategoria');
        const editIconoNombre = document.getElementById('editIconoNombre');

        // Al dar clic en el lápiz de editar: precargar el modal con los datos de esa fila
        document.querySelectorAll('.btn-edit-trigger').forEach(button => {
            button.addEventListener('click', function () {
                editCategoriaId.value = this.getAttribute('data-id');
                editNombreInput.value = this.getAttribute('data-nombre');
                editIconoNombre.textContent = this.getAttribute('data-icono');
                editIconoInput.value = ''; // limpiar selección de archivo previa

                editModal.show();
            });
        });

        // Mostrar el nombre del archivo cuando se selecciona uno nuevo
        editIconoInput.addEventListener('change', function () {
            editIconoNombre.textContent = this.files.length > 0
                ? this.files[0].name
                : 'Sin archivo';
        });

        // Guardar cambios (se tiene que cambiar en el backend)
        editForm.addEventListener('submit', function (e) {
            e.preventDefault();
            console.log('Guardando categoria:', {
                id: editCategoriaId.value,
                nombre: editNombreInput.value,
                icono: editIconoInput.files[0] || editIconoNombre.textContent
            });

            editModal.hide();
        });
    });
</script>
</body>
</html>
