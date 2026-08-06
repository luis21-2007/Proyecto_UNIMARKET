window.prepararModalEstado = function(idCategoria, nombreCategoria, accion) {
    // 1. Asignar el nombre de la categoría en la ventana modal
    var elNombre = document.getElementById('categoriaNombreConfirm');
    if (elNombre) elNombre.textContent = nombreCategoria;

    // 2. Definir si dice 'deshabilitar' o 'habilitar'
    var elTexto = document.getElementById('textoAccionModal');
    if (elTexto) {
        elTexto.textContent = (accion === 'desactivar') ? 'deshabilitar' : 'habilitar';
    }

    // 3. Modificar la URL del botón de confirmación
    var btnConfirm = document.getElementById('btnConfirmAction');
    if (btnConfirm) {
        btnConfirm.href = 'categorias?action=' + accion + '&id=' + idCategoria;
    }

    // 4. Mostrar el modal de Bootstrap manualmente
    var modalElement = document.getElementById('confirmModal');
    if (modalElement) {
        var myModal = bootstrap.Modal.getOrCreateInstance(modalElement);
        myModal.show();
    }
};

window.prepararModalEditar = function(idCategoria, nombreCategoria) {
    // 1. Cargar valores en los inputs del formulario
    var inputId = document.getElementById('editCategoriaId');
    var inputNombre = document.getElementById('editNombreCategoria');

    if (inputId) inputId.value = idCategoria;
    if (inputNombre) inputNombre.value = nombreCategoria;

    // 2. Mostrar el modal de edición
    var editModalElement = document.getElementById('editCategoriaModal');
    if (editModalElement) {
        var editModal = bootstrap.Modal.getOrCreateInstance(editModalElement);
        editModal.show();
    }
};