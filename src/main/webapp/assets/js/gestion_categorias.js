let categoriaIdSeleccionada = null;
let accionSeleccionada = null;

// Modal de Desactivar / Activar (Cambio de estado)
window.prepararModalEstado = function(idCategoria, nombreCategoria, accion) {
    categoriaIdSeleccionada = idCategoria;
    accionSeleccionada = accion;

    var elNombre = document.getElementById('categoriaNombreConfirm');
    if (elNombre) elNombre.textContent = nombreCategoria;

    var elTexto = document.getElementById('textoAccionModal');
    if (elTexto) {
        elTexto.textContent = (accion === 'desactivar') ? 'deshabilitar' : 'habilitar';
    }

    var modalElement = document.getElementById('confirmModal');
    if (modalElement) {
        var myModal = bootstrap.Modal.getOrCreateInstance(modalElement);
        myModal.show();
    }
};

// Evento de clic en Confirmar dentro del modal de estado
document.addEventListener('DOMContentLoaded', function() {
    var btnConfirm = document.getElementById('btnConfirmAction');
    if (btnConfirm) {
        btnConfirm.addEventListener('click', function(e) {
            e.preventDefault();
            if (!categoriaIdSeleccionada || !accionSeleccionada) return;

            const params = new URLSearchParams();
            params.append('action', accionSeleccionada);
            params.append('id', categoriaIdSeleccionada);

            fetch('categorias', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                },
                body: params
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        window.location.href = 'categorias?msg=' + (accionSeleccionada === 'desactivar' ? 'desactivada' : 'activada');
                    } else {
                        alert('Error: ' + data.message);
                    }
                })
                .catch(err => console.error('Error al cambiar estado:', err));
        });
    }

    // Evento Submit en el formulario de Edición
    var editForm = document.getElementById('editCategoriaForm');
    if (editForm) {
        editForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const id = document.getElementById('editCategoriaId').value;
            const nombre = document.getElementById('editNombreCategoria').value;

            const params = new URLSearchParams();
            params.append('action', 'update');
            params.append('categoriaId', id);
            params.append('nombre', nombre);

            fetch('categorias', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                },
                body: params
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        window.location.href = 'categorias?msg=actualizada';
                    } else {
                        alert('Error: ' + data.message);
                    }
                })
                .catch(err => console.error('Error al actualizar:', err));
        });
    }
});

// Modal de Edición
window.prepararModalEditar = function(idCategoria, nombreCategoria) {
    var inputId = document.getElementById('editCategoriaId');
    var inputNombre = document.getElementById('editNombreCategoria');

    if (inputId) inputId.value = idCategoria;
    if (inputNombre) inputNombre.value = nombreCategoria;

    var editModalElement = document.getElementById('editCategoriaModal');
    if (editModalElement) {
        var editModal = bootstrap.Modal.getOrCreateInstance(editModalElement);
        editModal.show();
    }
};